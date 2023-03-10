#if DEBUG

    import Combine
    import ProvbitShared
    func previewAnyObservable<ViewData: AnyObject>(
        _ viewData: ViewData
    ) -> AnyObservableViewData<ViewData> {
        observe(TestCommonState(viewData: viewData).viewData)
    }

//    class PreviewCommonState<ViewData: AnyObject>: CommonStateFlowObserver<ViewData> {
//        init(viewData: ViewData) {
//            super.init(from: TestCommonState(viewData: viewData).viewData)
//        }
//    }

#endif
