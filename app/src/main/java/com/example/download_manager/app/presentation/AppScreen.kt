package com.example.download_manager.app.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycling
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val permission = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    )

    val activityResult = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        it.data?.data?.let {folderUri->
            context.contentResolver.takePersistableUriPermission(
                folderUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
           // uri.value = folderUri
           // fileManager.setUri(folderUri)
        }
    }

    LaunchedEffect(key1 = Unit) {
        context.takePermission(
            version10 = {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                }
                activityResult.launch(intent)
            },
            versionBelow10 = {
                permission.launchMultiplePermissionRequest()
            }
        )
    }



    Column(Modifier.fillMaxSize()) {

    }
}


inline fun Context.takePermission(
    version10: () -> Unit,
    versionBelow10: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        version10.invoke()
    } else {
        versionBelow10.invoke()
    }

}