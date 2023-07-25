package com.digiventure.fido_exploration.pages.register

import android.app.Activity
import android.content.IntentSender
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.digiventure.fido_exploration.util.getApkKeyHash
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.api.common.AuthenticatorErrorResponse
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    navHostController: NavHostController,
    viewModel: RegisterViewModel
) {
    val token =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub25jZSI6IjM4OGUzZWYwLWJhYzMtNGY1Zi1hZmE0LTRkOTNiODkwYTQzMyIsImRpZCI6ImF3ZG5ha2puZGtuMTJrIiwiaWF0IjoxNjkwMzA3Nzc0LCJleHAiOjE2OTAzOTQxNzR9.syKSVk0vRnQRWGzOnJ1lWTgDxeeZhixtjJA6ZSt1j6k"
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun registerCredential(credential: PublicKeyCredential) {
        scope.launch {
            val result =
                viewModel.registerCredential(token, getApkKeyHash(context) ?: "", credential)
            if (result.isSuccess) {
                val data = result.getOrNull()
                Toast.makeText(context, data?.status ?: "", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val launchPendingIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val bytes = result.data?.getByteArrayExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)
        when {
            result.resultCode != Activity.RESULT_OK ->
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()

            bytes == null ->
                Toast.makeText(context, "Credential error", Toast.LENGTH_SHORT).show()

            else -> {
                val credential = PublicKeyCredential.deserializeFromBytes(bytes)
                val response = credential.response

                if (response is AuthenticatorErrorResponse) {
                    Toast.makeText(context, response.errorMessage ?: "", Toast.LENGTH_SHORT).show()
                } else {
                    registerCredential(credential)
                }
            }
        }
    }

    fun generateCredential() {
        scope.launch {
            val result = viewModel.generateCredential(
                token,
                getApkKeyHash(context) ?: ""
            )
            if (result.isSuccess) {
                val intent = result.getOrNull()
                if (intent != null) {
                    try {
                        launchPendingIntent.launch(IntentSenderRequest.Builder(intent).build())
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "tes") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back icon",
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Add New Credential") },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Add New Credential"
                    )
                },
                onClick = {
                    generateCredential()
                })
        },
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                ) {

                }
            }
        }
    )
}