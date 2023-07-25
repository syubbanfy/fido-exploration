package com.digiventure.fido_exploration.util

import com.digiventure.fido_exploration.model.AuthenticatorSelection
import com.digiventure.fido_exploration.model.CredentialDescriptor
import com.digiventure.fido_exploration.model.Option
import com.digiventure.fido_exploration.model.PubKeyCredParam
import com.digiventure.fido_exploration.model.Rp
import com.digiventure.fido_exploration.model.User
import com.google.android.gms.fido.fido2.api.common.Attachment
import com.google.android.gms.fido.fido2.api.common.AuthenticatorSelectionCriteria
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialCreationOptions
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialDescriptor
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialParameters
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialRpEntity
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialType
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialUserEntity
import com.google.android.gms.identity.sample.fido2.decodeBase64

fun parsePublicKeyCredentialCreationOptions(
    body: Option
): PublicKeyCredentialCreationOptions {
    val builder = PublicKeyCredentialCreationOptions.Builder()

    builder.setUser(parseUser(body.user))
    builder.setChallenge(body.challenge.decodeBase64())
    builder.setParameters(parseParameters(body.pubKeyCredParams))
    builder.setTimeoutSeconds(body.timeout.toDouble())
    builder.setExcludeList(parseCredentialDescriptors(body.excludeCredentials))
    builder.setAuthenticatorSelection(parseSelection(body.authenticatorSelection))
    builder.setRp(parseRp(body.rp))

    return builder.build()
}

private fun parseRp(reader: Rp): PublicKeyCredentialRpEntity {
    return PublicKeyCredentialRpEntity(reader.id, reader.name, /* icon */ null)
}

private fun parseUser(user: User): PublicKeyCredentialUserEntity {
    return PublicKeyCredentialUserEntity(
        user.id.decodeBase64(),
        user.name,
        "", // icon
        user.displayName
    )
}

private fun parseParameters(params: List<PubKeyCredParam>): List<PublicKeyCredentialParameters> {
    val parameters = mutableListOf<PublicKeyCredentialParameters>()
    
    params.forEach {
        parameters.add(PublicKeyCredentialParameters(it.type, it.alg.toInt()))    
    }
    
    return parameters
}

private fun parseCredentialDescriptors(
    reader: List<CredentialDescriptor>
): List<PublicKeyCredentialDescriptor> {
    val list = mutableListOf<PublicKeyCredentialDescriptor>()

    reader.forEach {
        list.add(
            PublicKeyCredentialDescriptor(
                PublicKeyCredentialType.PUBLIC_KEY.toString(),
                it.id.decodeBase64(),
                /* transports */ null
            )
        )
    }
    
    return list
}

private fun parseSelection(reader: AuthenticatorSelection): AuthenticatorSelectionCriteria {
    val builder = AuthenticatorSelectionCriteria.Builder()
    builder.setAttachment(reader.authenticatorAttachment?.let { Attachment.fromString(it) })

    return builder.build()
}