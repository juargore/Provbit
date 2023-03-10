import Foundation
import ProvbitShared
import SwiftUI

protocol ObservableViewData: ObservableObject {
    associatedtype ObservedViewData
    var viewData: ObservedViewData { get }
    var viewDataPublisher: Published<ObservedViewData>.Publisher { get }
    var viewDataPublished: Published<ObservedViewData> { get }
}

final class AnyObservableViewData<ViewDataType>: ObservableViewData {
    typealias ObservedViewData = ViewDataType

    private let viewDataGetter: () -> ObservedViewData

    var viewData: ObservedViewData { viewDataGetter() }
    var viewDataPublisher: Published<ObservedViewData>.Publisher
    var viewDataPublished: Published<ObservedViewData>

    /// Due to the manner in which we initialize our `ViewModel` objects, they can be
    /// greedily deinited after being associated with a view. This maintains a reference
    /// to that underlying observed obejct so it isn't removed until we're done observing
    /// it. This type will always be the same as `OVD` found in the constructor.
    private var keptRef: Any

    init<OVD: ObservableViewData>(wrappedObservableViewData: OVD)
        where OVD.ObservedViewData == ViewDataType {
        keptRef = wrappedObservableViewData
        viewDataGetter = { wrappedObservableViewData.viewData }
        viewDataPublisher = wrappedObservableViewData.viewDataPublisher
        viewDataPublished = wrappedObservableViewData.viewDataPublished
    }
}

class CommonStateFlowObserver<ViewData: AnyObject>: ObservableViewData {
    typealias ObservedViewData = ViewData

    var viewDataPublisher: Published<ObservedViewData>.Publisher { $viewData }
    var viewDataPublished: Published<ObservedViewData> { _viewData }

    @Published private(set) var viewData: ViewData

    /// This class directly observes view data from a `CommonStateFlow`
    /// As such, it needs to be able to unsubscribe from these changes, which requires
    /// cancelling a scope in the `shared` module. These `Closeable` objects
    /// wrap this ability, and we call it on `deinit`.
    private var closer: Closeable?

    init(from commonFlow: CommonStateFlow<ViewData>) {
        viewData = commonFlow.currentValue!
        closer = commonFlow.watch { [weak self] viewData in
            guard let viewData = viewData else { return }
            guard let self = self else { return }
            self.viewData = viewData
        }
    }

    deinit {
        closer?.close()
    }
}

func observe<T>(_ flow: CommonStateFlow<T>) -> AnyObservableViewData<T> {
    AnyObservableViewData(wrappedObservableViewData: CommonStateFlowObserver(from: flow))
}
