import AVKit

/// A protocol that is intended to wrap AVFoundation's `AVCaptureDevice` methods.
protocol AVCaptureDeviceProvbit {
    /// A method to match the signature of `AVCaptureDevice` `requestAccess` type method.
    static func requestAccess(for mediaType: AVMediaType, completionHandler handler: @escaping (Bool) -> Void)
}

/// Extends AVFoundation's type to conform to `AVCaptureDeviceProvbit`.
extension AVCaptureDevice: AVCaptureDeviceProvbit {}
