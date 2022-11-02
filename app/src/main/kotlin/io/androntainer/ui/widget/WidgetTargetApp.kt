package io.androntainer.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import io.androntainer.databinding.WidgetTargetAppBinding


@Composable
fun WidgetTargetApp(
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    targetAppChecked: () -> Unit,
    targetAppUnchecked: () -> Unit,
){
    AndroidViewBinding(
        factory = WidgetTargetAppBinding::inflate
    ){
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) targetAppChecked() else targetAppUnchecked()
        }
        title.text = targetAppName
        versionName.text = targetAppVersionName
        packageName.text = targetAppPackageName
        description.text = targetAppDescription
    }
}

@Preview
@Composable
fun WidgetTargetAppPreview(){
    WidgetTargetApp(
        targetAppName = "Androntainer",
        targetAppPackageName = "unknown",
        targetAppDescription = "unknown",
        targetAppVersionName = "1.0",
        targetAppChecked = {},
        targetAppUnchecked = {}
    )
}