package com.example.doubanmovie.ui.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun MainScreen(onNavigateToDetail: () -> Unit = {}) {
    Column {
        SearchBar()
        DouMainBody(onNavigateToDetail)
    }
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun DouMainBody(
    onNavigateToDetail: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    Column {
        TabBar(pageState = pagerState ,coroutineScope=coroutineScope)

        HorizontalPager(
            state = pagerState,
            count = tab_items.size,
            modifier = Modifier.weight(weight = 1f)
        ) { index ->
            when(index) {
                0-> MovieBody(onNavigateToDetail)
                1-> Text1()
                2-> Text1()
                3-> Text1()
                4-> Text1()
                5-> Text1()
            }
        }
    }

//    LazyColumn(state = listState){
//        stickyHeader {
//            TabBar(pageState = pagerState ,coroutineScope=coroutineScope)
//        }
//
//        item {
//            HorizontalPager(
//                state = pagerState,
//                count = tab_items.size,
////                modifier = Modifier.fillMaxSize()
////                modifier = Modifier.weight(weight = 1f)
//            ) { index ->
//                when(index) {
//                    0-> MovieBody(onNavigateToDetail)
//                    1-> Text1()
//                    2-> Text1()
//                    3-> Text1()
//                    4-> Text1()
//                    5-> Text1()
//                }
//            }
//
//        }
//
//    }
}



val tab_items = listOf(
    "电影", "电视",
    "读书", "连载",
    "音乐", "同城"
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabBar(pageState: PagerState , coroutineScope: CoroutineScope) {

    TabRow(
        selectedTabIndex = pageState.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[pageState.currentPage]),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

    ) {
        tab_items.forEachIndexed { index, item ->
            Tab(
                modifier = Modifier.height(48.dp),
                selected = pageState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pageState.scrollToPage(index)
                    }
//                    pageState.currentPage = index
                }) {
                Text(text = item)
            }
        }
    }
}







@Composable
fun SearchBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { }
        ) {
            Icon(Icons.Filled.Menu, "menu")
        }
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            color = Color.LightGray.copy(0.3f),
            contentColor = Color.Gray,
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        Log.i('1'.toString(), "1211");

                    }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                )
                Text("抬头, 看树", style = MaterialTheme.typography.bodySmall)
            }.apply {  }
        }
        IconButton(
            onClick = { }
        ) {
            Icon(Icons.Filled.MailOutline, "message")
        }
    }
}


























