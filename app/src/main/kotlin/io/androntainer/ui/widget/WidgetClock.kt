package io.androntainer.ui.widget

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import io.androntainer.R

@Composable
fun WidgetClock(){
    AndroidView(
        factory = { context ->
            LayoutInflater.from(context)
                .inflate(R.layout.widget_clock, null)
        }
    )
}

@Preview
@Composable
fun WidgetClockPreview(){
    WidgetClock()
}