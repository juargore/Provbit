import Foundation
import ProvbitShared
import SwiftUI

// MARK: - ViewModel

/// This `ViewModel` class matched the paired  `ProcessorViewModel` class in the Android codebase.
/// The purpose of this class is to observe both view data and events from a `Processor`. This implementation
/// will forward events onto a provided `EventHandler`, and emit `ViewData` over an `@Published` variable.
class ViewModel<ViewData: AnyObject, Event: AnyObject>: ObservableObject, ObservableViewData {
    typealias ObservedViewData = ViewData
    @Published private(set) var viewData: ViewData
    var viewDataPublisher: Published<ObservedViewData>.Publisher { $viewData }
    var viewDataPublished: Published<ObservedViewData> { _viewData }

    /// The view model directly observes `Processor` view data and events.
    /// As such, it needs to be able to unsubscribe from these changes, which requires
    /// cancelling a scope in the `shared` module. These `Closeable` objects
    /// wrap this ability, and we call it on `deinit`.
    /// Additionally, the `Processor` itself is a scoped object that must be cancelled
    /// when it is done being used, which is also done on `deinit`.
    private var viewDataCloser: Closeable?
    private var eventCloser: Closeable?
    private var processor: Processor<ViewData, Event>

    private var handle: (Event) -> Void = { fatalError("ViewModel cannot handle event: \($0)") }

    init<H: EventHandler>(
        processor: Processor<ViewData, Event>,
        eventHandler: H
    ) where H.Event == Event {
        self.processor = processor
        viewData = processor.viewData.currentValue!
        handle = { eventHandler.handle(event: $0) }

        viewDataCloser = processor.viewData.watch { [weak self] watchedViewData in
            guard let self = self else { return }
            self.viewData = watchedViewData!
        }

        eventCloser = processor.events.watch { [weak self] watchedEvent in
            guard let self = self else { return }
            self.handle(watchedEvent!)
        }
    }

    deinit {
        viewDataCloser?.close()
        eventCloser?.close()
        processor.close()
    }
}

func observe<T, A: AnyObject>(_ viewModel: ViewModel<T, A>) -> AnyObservableViewData<T> {
    AnyObservableViewData(wrappedObservableViewData: viewModel)
}
