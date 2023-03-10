import SwiftUI

// MARK: - SplashView

/// A simple splash screen.
struct SplashView: View {
    var body: some View {
        Text("SPLASH")
            .font(.system(.largeTitle, design: .rounded).bold())
    }
}

#if DEBUG

    struct SlashView_Previews: PreviewProvider {
        static var previews: some View {
            Group {
                ForEach(ColorScheme.allCases, id: \.self) {
                    SplashView().preferredColorScheme($0)
                }
            }
        }
    }

#endif
