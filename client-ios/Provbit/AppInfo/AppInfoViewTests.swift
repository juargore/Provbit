import ViewInspector
import XCTest

@testable import Provbit
@testable import ProvbitShared

extension AppInfoView: Inspectable {}

extension AppInfoViewData {
    static func fixture(
        tapCountLabel tapCountLabelFixture: String = "0 taps",
        tapCountButtonLabel tapCountButtonLabelFixture: String = "Tap to count",
        toast toastFixture: ToastViewData = ToastViewData.Hide(),
        testButtonOnClick testButtonOnClickFixture: @escaping () -> Void = {},
        itemsButtonOnClick itemsButtonOnClickFixture: @escaping () -> Void = {},
        qrResultsButtonOnClick qrResultsButtonOnClickFixture: @escaping () -> Void = {},
        itemsSelectorButtonOnClick itemsSelectorButtonOnClickFixture: @escaping () -> Void = {},
        versionInfoLabel versionInfoLabelFixture: String = "Version 0.0.0",
        viewListButtonLabel viewListButtonLabelFixture: String = "Go to List",
        viewSelectorButtonLabel viewSelectorButtonLabelFixture: String = "View Selector"
    ) -> AppInfoViewData {
        .init(
            tapCountLabel: PreviewString(tapCountLabelFixture),
            tapCountButtonLabel: PreviewString(tapCountButtonLabelFixture),
            toast: toastFixture,
            testButtonOnClick: testButtonOnClickFixture,
            itemsButtonOnClick: itemsButtonOnClickFixture,
            itemSelectorButtonOnClick: itemsSelectorButtonOnClickFixture,
            qrResultsButtonOnClick: qrResultsButtonOnClickFixture,
            versionInfoLabel: PreviewString(versionInfoLabelFixture),
            viewListButtonLabel: PreviewString(viewListButtonLabelFixture),
            viewSelectorButtonLabel: PreviewString(viewSelectorButtonLabelFixture)
        )
    }
}

class AppInfoViewTests: XCTestCase {
    var viewData: AppInfoViewData!
    var subject: AppInfoView!

    override func setUp() {
        super.setUp()
        viewData = .fixture()
        subject = AppInfoView(viewModel: previewAnyObservable(viewData))
    }

    func testCountButtonAction() {
        let buttonText = "tap it"
        var buttonWasPressed = false
        viewData = .fixture(
            tapCountButtonLabel: buttonText,
            testButtonOnClick: {
                buttonWasPressed = true
            }
        )
        subject = AppInfoView(viewModel: previewAnyObservable(viewData))

        XCTAssertNotNil(try subject.inspect().find(button: buttonText))
        do {
            try subject.inspect().find(button: buttonText).tap()
        } catch {
            XCTFail("Could not tap button.")
        }
        XCTAssertTrue(buttonWasPressed)
    }

    func testListButtonAction() {
        let buttonText = "go to the list!"
        var buttonWasPressed = false
        viewData = .fixture(
            itemsButtonOnClick: {
                buttonWasPressed = true
            },
            viewListButtonLabel: buttonText
        )
        subject = AppInfoView(viewModel: previewAnyObservable(viewData))

        XCTAssertNotNil(try subject.inspect().find(button: buttonText))
        do {
            try subject.inspect().find(button: buttonText).tap()
        } catch {
            XCTFail("Could not tap button.")
        }
        XCTAssertTrue(buttonWasPressed)
    }

    func testVersionInfo() {
        XCTAssertEqual(try subject.body.inspect().vStack().text(0).string(), viewData.versionInfoLabel.localized())
    }
}
