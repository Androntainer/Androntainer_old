package io.androntainer.ui.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import io.androntainer.databinding.WidgetControlBinding

@Composable
fun WidgetControl(
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SelectOnClick: () -> Unit
){
    AndroidViewBinding(
        factory = WidgetControlBinding::inflate
    ){
        controlDrawer.setOnClickListener {
            NavigationOnClick()
        }
        controlMenu.setOnClickListener {
            MenuOnClick()
        }
        controlApps.setOnClickListener {
            AppsOnClick()
        }
        controlSearch.setOnClickListener {
            SearchOnClick()
        }
        controlSelect.setOnClickListener {
            SelectOnClick()
        }
    }
}

@Preview
@Composable
fun WidgetControlPreview(){
   WidgetControl(
       NavigationOnClick = {},
       MenuOnClick = {},
       AppsOnClick = {},
       SearchOnClick = {},
       SelectOnClick = {}
   )
}