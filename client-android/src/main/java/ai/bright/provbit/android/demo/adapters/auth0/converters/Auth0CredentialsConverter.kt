package ai.bright.provbit.android.demo.adapters.auth0.converters

import ai.bright.provbit.architecture.Converter
import ai.bright.provbit.demo.domain.ports.AuthenticationRepository.Credentials
import ai.bright.provbit.demo.entities.Timestamp
import com.auth0.android.result.Credentials as Auth0Credentials

class Auth0CredentialsConverter : Converter<Auth0Credentials, Credentials> {

    override fun convertInbound(value: Auth0Credentials): Credentials = Credentials(
        idToken = value.idToken,
        accessToken = value.accessToken,
        expiration = Timestamp(
            epochSeconds = value.expiresAt.time / 1_000 // Note: time is ms, so we need to get to seconds.
        )

    )
}
