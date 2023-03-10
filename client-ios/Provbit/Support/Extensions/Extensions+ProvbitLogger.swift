import ProvbitShared

// MARK: - ProvbitLogger

/// Extension of ProvbitLogger to handle optional "persistLogs" parameter
///
extension ProvbitLogger {
    convenience init<O: AnyObject>(object: O.Type) {
        self.init(tag: String(describing: object), persistLogs: false)
    }

    convenience init<O: AnyObject>(object: O.Type, persistLogs: Bool) {
        self.init(tag: String(describing: object), persistLogs: KotlinBoolean(bool: persistLogs))
    }
}
