package ai.bright.provbit.demo.adapters.sqlite

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

// in src/commonMain/kotlin
actual class DriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(AppDatabase.Schema, "AppDatabase.db")
}