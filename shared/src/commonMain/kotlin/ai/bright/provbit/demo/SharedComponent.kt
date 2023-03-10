package ai.bright.provbit.demo

import ai.bright.provbit.demo.adapters.bluetooth.BluetoothContext
import ai.bright.provbit.demo.adapters.bluetooth.BluetoothGateway
import ai.bright.provbit.demo.adapters.bluetooth.NativeBluetoothConnectionGateway
import ai.bright.provbit.demo.adapters.conch.ports.ConchHardwareRepository
import ai.bright.provbit.demo.adapters.local.ports.LocalUserRepository
import ai.bright.provbit.demo.adapters.realm.models.RealmItem
import ai.bright.provbit.demo.adapters.sqlite.AppDatabase
import ai.bright.provbit.demo.adapters.sqlite.DriverFactory
import ai.bright.provbit.demo.adapters.sqlite.models.ItemDbQueries
import ai.bright.provbit.demo.adapters.sqlite.ports.SqlItemRepository
import ai.bright.provbit.demo.domain.ports.*
import ai.bright.provbit.demo.domain.uses.ai.ImageRecognitionUseCase
import ai.bright.provbit.demo.domain.uses.hardware.GetHardwareUseCase
import ai.bright.provbit.demo.domain.uses.items.*
import ai.bright.provbit.demo.domain.uses.theme.CurrentThemeFlow
import ai.bright.provbit.demo.domain.uses.user.*
import ai.bright.provbit.demo.injection.NetworkingComponent
import ai.bright.provbit.demo.presentation.*
import io.realm.Realm
import io.realm.RealmConfiguration
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope

@Component
@AppScope
abstract class SharedComponent(
    @get:Provides protected val driverFactory: DriverFactory,
    @get:Provides protected val authRepository: AuthenticationRepository,
    @get:Provides protected val ImageRecognitionRepository: ImageRecognitionRepository,
    @get:Provides protected val context: BluetoothContext
) : NetworkingComponent {

    @AppScope
    // Switch this when testing live bluetooth connections:
     val NativeBluetoothConnectionGateway.bind: BluetoothGateway
//    val CannedBluetoothGateway.bind: BluetoothGateway
        @Provides get() = this

    @AppScope
    val ConchHardwareRepository.bind: HardwareRepository
        @Provides get() = this

    @AppScope
    @Provides
    fun appDatabase(driverFactory: DriverFactory): AppDatabase =
        AppDatabase(driverFactory.createDriver())

    @AppScope
    @Provides
    fun itemQueries(appDatabase: AppDatabase): ItemDbQueries =
        appDatabase.itemDbQueries

    @AppScope
    @Provides
    fun realm(): Realm = Realm.open(
        RealmConfiguration.with(schema = setOf(RealmItem::class))
    )

    // TODO: Figure out if this is needed both here and on the class definition -Ben
    /*
    Uncomment realm bind below to try local persistence with Realm.
    Be sure to comment out the SqlItemRepository binding below.
     */
//    @AppScope
//    val RealmItemRepository.bind: ItemRepository
//        @Provides get() = this

    @AppScope
    val SqlItemRepository.bind: ItemRepository
        @Provides get() = this

    @AppScope
    val LocalUserRepository.bind: UserRepository
        @Provides get() = this

    val GetItemDetailUseCase.Default.bind: GetItemDetailUseCase
        @Provides get() = this

    val SignInUseCase.Default.bind: SignInUseCase
        @Provides get() = this

    val ActiveUserFlow.Default.bind: ActiveUserFlow
        @Provides get() = this

    val QueryItemsUseCase.Default.bind: QueryItemsUseCase
        @Provides get() = this

    val AddItemUseCase.Default.bind: AddItemUseCase
        @Provides get() = this

    val DeleteItemUseCase.Default.bind: DeleteItemUseCase
        @Provides get() = this

    val UpdateItemUseCase.Default.bind: UpdateItemUseCase
        @Provides get() = this

    val Auth0SignInUseCase.Default.bind: Auth0SignInUseCase
        @Provides get() = this

    val Auth0SignOutUseCase.Default.bind: Auth0SignOutUseCase
        @Provides get() = this

    val GetHardwareUseCase.Default.bind: GetHardwareUseCase
        @Provides get() = this

    val CurrentAuthStateUseCase.Default.bind: CurrentAuthStateUseCase
        @Provides get() = this

    val CurrentThemeFlow.Default.bind: CurrentThemeFlow
        @Provides get() = this

    val ImageRecognitionUseCase.Default.bind: ImageRecognitionUseCase
        @Provides get() = this

    abstract val appInfoProcessor: () -> AppInfoProcessor

    abstract val itemDetailProcessor: (String) -> ItemDetailProcessor

    abstract val loginProcessor: () -> LoginProcessor

    abstract val navProcessor: () -> NavProcessor

    abstract val itemListProcessor: () -> ItemListProcessor

    abstract val multiSelectorDemoProcessor: () -> MultiSelectorDemoProcessor

    abstract val formProcessor: () -> FormProcessor

    abstract val qrScanProcessor: () -> QRScanProcessor

    abstract val signInProcessor: () -> SignInProcessor

    abstract val styleGuideProcessor: () -> StyleGuideProcessor

    abstract val imageAnalysisProcessor: () -> ImageAnalysisProcessor

    abstract val bluetoothScanProcessor: () -> BluetoothScanProcessor

    companion object
}

@Scope
annotation class AppScope
