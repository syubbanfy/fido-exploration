package com.digiventure.fido_exploration.pages.register

import android.app.Activity
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    navHostController: NavHostController,
    viewModel: RegisterViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val launchPendingIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val bytes = result.data?.getByteArrayExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)
        when {
            result.resultCode != Activity.RESULT_OK ->
                Log.e("error", "cancelled")
            bytes == null ->
                Log.e("error", "credential error")
            else -> {
                val credential = PublicKeyCredential.deserializeFromBytes(bytes)
                val response = credential.response

                if (response is AuthenticatorErrorResponse) {
                    Log.e("error auth", response.errorMessage ?: "")
                } else {
                    Log.d("id", credential.id)
                    Log.d("rawId", String(credential.rawId))
                    Log.d("type", credential.type)
                    Log.d("response", Gson().toJson(credential.response.clientDataJSON))
                    Log.d("clientExtensionResults", Gson().toJson(credential.clientExtensionResults))
                }
            }
        }
    }

    fun register() {
        scope.launch {
            val result = viewModel.register("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub25jZSI6IjMzMDAxOWViLWRmMWItNGE1Ni1iOGQ5LTE2NjcwMDhlMDc2NSIsImRpZCI6ImRpZDpldGhyOjB4NUM0OEUyZjQyQUMxOTE3NjI2M0RBZkNDYjEwODcyRDVjRWExZDEwQSIsImlhdCI6MTY5MDE2Mjg4NSwiZXhwIjoxNjkwMjQ5Mjg1fQ.FLSwYG8B9foHCo5oNg1PkSFjhsorok8w7X60n_TfJpc")
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
                    register()
//                    getApkKeyHash(context)?.let { Log.d("hash", it) }
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