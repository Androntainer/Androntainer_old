package io.androntainer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.twotone.ArrowRight
import androidx.compose.material.icons.twotone.Science
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.androntainer.ui.values.AndrontainerTheme
import io.androntainer.ui.widget.WidgetClock
import io.androntainer.ui.widget.WidgetControl
import io.androntainer.ui.widget.WidgetTargetApp
import kotlinx.coroutines.launch

@Composable
fun ScreenHome(
    title: String,
    targetAppName: String,
    targetAppPackageName: String,
    targetAppDescription: String,
    targetAppVersionName: String,
    NavigationOnClick: () -> Unit,
    MenuOnClick: () -> Unit,
    SearchOnClick: () -> Unit,
    SheetOnClick: () -> Unit,
    AppsOnClick: () -> Unit,
    SelectOnClick: () -> Unit,
    onNavigateToApps: () -> Unit,
){
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val expandedMenu = remember { mutableStateOf(false) }
    val expandedPowerButton = remember { mutableStateOf(true) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.body1
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            //NavigationOnClick()
                            scope.launch {
                                scaffoldState.drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            expandedMenu.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                    }
                    DropdownMenu(
                        expanded = expandedMenu.value,
                        onDismissRequest = {
                            expandedMenu.value = false
                        },
                    ) {
                        DropdownMenuItem(
                            onClick = {

                                expandedMenu.value = false
                            }
                        ) {
                            Text("分享")
                        }
                        Divider()
                        DropdownMenuItem(
                            onClick = {
                                scope.launch {
                                    scaffoldState.drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                                expandedMenu.value = false
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("抽屉")
                        }
                        DropdownMenuItem(
                            onClick = {
                                expandedMenu.value = false
                                MenuOnClick()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("更多")
                        }
                    }
                },
                elevation = 10.dp
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                cutoutShape = MaterialTheme.shapes.small.copy(
                    CornerSize(percent = 50)
                ),
                elevation = 10.dp,
                content = {
                    TopAppBar(
                        title = {
                            Text(
                                text = targetAppName,
                                style = MaterialTheme.typography.body1
                            )
//                                IconButton(
//                                    onClick = {
//                                        scope.launch {
//                                            scaffoldState.snackbarHostState
//                                                .showSnackbar(targetAppName)
//                                        }
//                                    }
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Filled.Link,
//                                        contentDescription = null,
//                                        modifier = Modifier.fillMaxHeight(),
//                                    )
//                                }
                        },
                        modifier = Modifier
                            .fillMaxSize(),
                        navigationIcon = {
                            IconButton(
                                onClick = SelectOnClick
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SelectAll,
                                    contentDescription = null
                                )
                            }
                        },
                        elevation = 0.dp
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToApps,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        drawerContent = {
//                Box(
//                    modifier = Modifier.fillMaxSize()
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.background),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.FillBounds
//                    )
//                    Column(
//                        modifier = Modifier.fillMaxSize()
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .height(IntrinsicSize.Min)
//                                .fillMaxWidth()
//                                .padding(all = 5.dp),
//                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxHeight()
//                                    .weight(1f),
//                                horizontalAlignment = Alignment.Start,
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                Card(
//                                    modifier = Modifier
//                                        .clickable {
//                                            SearchOnClick()
//                                        }
//                                        .padding(5.dp),
//                                    shape = MaterialTheme.shapes.small.copy(
//                                        CornerSize(percent = 50)
//                                    ),
//                                    elevation = 10.dp
//                                ) {
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.ic_baseline_google_24),
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .padding(
//                                                start = 25.dp,
//                                                end = 25.dp,
//                                                top = 10.dp,
//                                                bottom = 10.dp
//                                            ),
//                                        tint = MaterialTheme.colors.primary
//                                    )
//                                }
//                            }
//                            Column(
//                                modifier = Modifier.padding(end = 15.dp),
//                                verticalArrangement = Arrangement.Center
//                            ) {
//                                WidgetClock()
//                            }
//                        }
//                    }
//                }


            Text("Drawer title", modifier = Modifier.padding(16.dp))
            Divider()
        },
        drawerGesturesEnabled = true
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
//                Image(
//                    painter = painterResource(id = R.drawable.background),
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.FillBounds
//                )
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "当前时间",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WidgetClock()
                    }
                }

                Divider()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "控制台",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WidgetControl(
                            NavigationOnClick = {
                                //NavigationOnClick()
                                scope.launch {
                                    scaffoldState.drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            },
                            MenuOnClick = {
                                expandedMenu.value = true
                            },
                            AppsOnClick = onNavigateToApps,
                            SearchOnClick = SearchOnClick,
                            SelectOnClick = SelectOnClick
                        )
                    }
                }
                Divider()
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "目标应用",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        WidgetTargetApp(
                            targetAppName = targetAppName,
                            targetAppPackageName = targetAppPackageName,
                            targetAppDescription = targetAppDescription,
                            targetAppVersionName = targetAppVersionName,
                            targetAppChecked = {
                                expandedPowerButton.value = true
                            },
                            targetAppUnchecked = {
                                expandedPowerButton.value = false
                            }
                        )
                    }
                }
                Divider()
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "电源",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 5.dp
                            )
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(end = 2.5.dp),
                            enabled = expandedPowerButton.value
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ToggleOn,
                                    contentDescription = null,
                                    tint = Color.Green
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(
                                    text = "ON",
                                    color = Color.Green
                                )
                            }
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 2.5.dp),
                            enabled = expandedPowerButton.value
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ToggleOff,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(
                                    text = "OFF",
                                    color = Color.Red
                                )
                            }

                        }
                    }
                }
                Divider()
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "设备信息",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp,
                                bottom = 2.5.dp
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 2.5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Android,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "sb",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = 5.dp,
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 2.5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhoneAndroid,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "sb",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = 5.dp,
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 2.5.dp,
                                bottom = 2.5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DesignServices,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                        Text(
                            text = "sb",
                            modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = 5.dp,
                                )
                        )
                    }
                }
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        shape = MaterialTheme.shapes.small.copy(
//                            CornerSize(percent = 10)
//                        ),
//                        backgroundColor = Color(0x99FFFFFF),
//                        elevation = 0.dp,
//                    ) {
//
//                    }

//                    AndroidViewBinding(
//                        factory = FactoryMainBinding::inflate,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding),
//                    ) {
//
//                    }


            }
        }
    }
}

@Preview
@Composable
fun ScreenHomePreview(){
    AndrontainerTheme {
        ScreenHome(
            title = "Androntainer",
            targetAppName = "Androntainer",
            targetAppPackageName = "Androntainer",
            targetAppDescription = "Androntainer",
            targetAppVersionName = "1.0",
            NavigationOnClick = {},
            MenuOnClick = {},
            SearchOnClick = {},
            SheetOnClick = {},
            onNavigateToApps = {},
            AppsOnClick = {},
            SelectOnClick = {}
        )
    }
}