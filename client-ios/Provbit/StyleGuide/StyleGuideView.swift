import ProvbitShared
import SwiftUI

struct StyleGuideView: View {
    @ObservedObject var component: AnyObservableViewData<StyleGuideViewData>

    var body: some View {
        ScrollView {
            VStack {
                Text(component.viewData.headerTextSection.localized())
                    .font(.withStyle(.headline1))
                    .padding()

                Text(component.viewData.textLabelH1.localized())
                    .font(.withStyle(.headline1))
                Text(component.viewData.textLabelH2.localized())
                    .font(.withStyle(.headline2))
                Text(component.viewData.textLabelPlain.localized())
                    .font(.withStyle(.plain))

                Text(component.viewData.headerButtonSection.localized())
                    .font(.withStyle(.headline1))
                    .padding()

                ButtonView(state: component.viewData.primaryButton)
                ButtonView(state: component.viewData.secondaryButton)
                ButtonView(state: component.viewData.tertiaryButton)
            }
        }
        .navigationTitle("Style Guide")
    }
}

#if DEBUG
//
//    struct StyleGuideView_Previews: PreviewProvider {
//        static var previews: some View {
//            Group {
//                NavigationView {
//                    StyleGuideView(viewModel: ViewModel(
//                        processor: PreviewProcessor(
//                            viewData: StyleGuideViewData(
//                                buttonDummyOnClick: {},
//                                buttonLabelLarge: PreviewString("Button Large"),
//                                buttonLabelMedium: PreviewString("Button Medium"),
//                                buttonLabelSmall: PreviewString("Button Small"),
//                                headerButtonSection: PreviewString("Button Styles"),
//                                headerTextSection: PreviewString("Text Styles"),
//                                textLabelH1: PreviewString("Header 1"),
//                                textLabelH2: PreviewString("Header 2"),
//                                textLabelPlain: PreviewString("Plain text style")
//                            )
//                        )
//                    ))
//                }
//            }
//        }
//    }

#endif
