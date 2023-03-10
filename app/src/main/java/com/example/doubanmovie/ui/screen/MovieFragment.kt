package com.example.doubanmovie.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.fragment.app.Fragment
import com.example.doubanmovie.R
import com.example.doubanmovie.data.MovieComment
import com.example.doubanmovie.data.MovieItem
import com.example.doubanmovie.ui.components.AsyncImage
import com.example.doubanmovie.ui.theme.DoubanMovieTheme
import com.example.doubanmovie.ui.theme.LightGrey
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

/**
 * @Des???
 * @author: moon
 * @date: 3/7/23
 */
class MovieFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment{
            val args = Bundle()
            val fragment = MovieFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
//                MovieBody()
                Text1()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieBody(
    onNavigateToDetail: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(state = listState) {

        item {
            NavList(onSearch = {
                coroutineScope.launch {
                    // Animate scroll to the 10th item
                    listState.animateScrollToItem(index = 6)
                }
            })
        }
        item {

            HitTheaters(onNavigateToDetail = onNavigateToDetail)
        }
        item {

            ComingMovie()
        }
        item {

            MovieRank(onNavigateToDetail = onNavigateToDetail)
        }
        item {
            SearchMovieHead()
        }
        stickyHeader {
            SearchMovieBody(onFilterItemClick = {
                coroutineScope.launch {
                    // Animate scroll to the 10th item
                    listState.scrollToItem(index = 6)
                }
            })
        }
        item {
            SearchMovieList(onNavigateToDetail = onNavigateToDetail)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavList(onSearch: () -> Unit = {}) {
    val navList = listOf(
        NavItem("?????????", R.drawable.ic_subjects_categories_movie),
        NavItem("????????????", R.drawable.ic_subjects_ranking),
        NavItem("????????????", R.drawable.ic_subjects_time),
        NavItem("????????????", R.drawable.ic_subjects_movie_lists),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        navList.forEachIndexed { index, navItem ->
            ElevatedCard(
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(20),
                onClick = if (index == 0) onSearch else ({})
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(navItem.icon), null)
                    Text(
                        text = navItem.name,
                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.alpha(ContentAlpha.medium)
                    )
                }
            }
        }
    }
}

@Composable
fun HitTheaters(onNavigateToDetail: () -> Unit = {}) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        HitHeader()
        HitBody(onNavigateToDetail = onNavigateToDetail)
    }
}

@Composable
fun HitHeader() {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HitTabbar()
        Spacer(modifier = Modifier.weight(1f))
        ShowMore()
    }
}

@Composable
fun HitBody(onNavigateToDetail: () -> Unit = {}) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        item {}
        items(movieList) { item ->
            Column {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onNavigateToDetail),

                    ) {
                    AsyncImage(
                        model = item.image,
                        modifier = Modifier
                            .width(110.dp)
                            .height(160.dp),
                    )
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(bottomEnd = 8.dp)),
                        color = Color.Black.copy(alpha = 0.3f),
                        contentColor = Color.White,
                    ) {
                        Icon(
                            modifier = Modifier.padding(2.dp),
                            imageVector = if (item.favorite) Icons.Outlined.Done else Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                com.example.doubanmovie.ui.components.RatingBar(rating = item.rating)

            }
        }
        item {}
    }
}


@Composable
fun ShowMore() {
    val total = 33
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "??????$total", style = MaterialTheme.typography.bodySmall)
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun HitTabbar() {
    val hitList = listOf("????????????", "????????????")
    var currentIndex by remember {
        mutableStateOf(0)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        hitList.forEachIndexed { index, item ->
            if (index > 0) {
                Divider(
                    modifier = Modifier
                        .padding(PaddingValues(horizontal = 16.dp))
                        .height(24.dp)
                        .width(1.dp)
                )
            }
            Tab(
                modifier = Modifier.width(IntrinsicSize.Max),
                selected = currentIndex == index,
                onClick = {
                    currentIndex = index
                }) {

                Text(
                    text = item,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComingMovie() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        comingMovieList.forEach { item ->
            ElevatedCard(
                modifier = Modifier.weight(1f),
                onClick = {}
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = item.title, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.desc,
                            style = MaterialTheme.typography.bodySmall,
//                            modifier = Modifier.alpha(ContentAlpha.medium),
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        AsyncImage(
                            model = item.image,
                            modifier = Modifier
                                .width(55.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(10.dp)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieRank(
    onNavigateToDetail: () -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(vertical = 16.dp)
            .height(240.dp)
    ) {
        item {}
        items(movieRankList) { item ->
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp),
                shape = RoundedCornerShape(10.dp),
                contentColor = Color.White,
            ) {
                AsyncImage(
                    model = item.image,
                    modifier = Modifier
                        .fillMaxSize(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                0.1f to Color(0x00FFFFFF),
                                0.4f to item.color,
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(PaddingValues(16.dp)),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = item.title, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "????????????", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            item.items.forEachIndexed { index, movieItem ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = onNavigateToDetail),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(text = (index + 1).toString())
                                    AsyncImage(
                                        model = movieItem.image,
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(42.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                    )
                                    Column {
                                        Text(
                                            text = movieItem.name,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        com.example.doubanmovie.ui.components.RatingBar(rating = movieItem.rating)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Surface(
                modifier = Modifier.fillMaxHeight(),
                color = LightGrey,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.width(14.dp),
                        text = "??????",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = "more",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        item {}
    }
}

@Composable
fun SearchMovieHead() {
    Surface(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
        Text(
            text = "?????????",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun SearchMovieBody(onFilterItemClick: () -> Unit = {}) {
    var current by remember {
        mutableStateOf(-1)
    }
    val tagList = listOf(
        "??????", "??????", "?????????", "??????",
        "??????", "??????", "??????", "??????",
        "??????", "??????", "??????", "??????",
    )
    Surface {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CanPlay()
                filterList.forEachIndexed { index, filterItem ->
                    FilterItem(
                        data = filterItem,
                        selected = current == index,
                        onClick = {
                            current = if (current == index) {
                                -1
                            } else {
                                onFilterItemClick()
                                index
                            }
                        }
                    )
                }
                FilterMore()
            }
            Box {
                if (current >= 0) {
                    val items = filterList[current].items
                    Popup(
                        alignment = Alignment.TopStart,
                        properties = PopupProperties(
                            clippingEnabled = false
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Black.copy(alpha = 0.1f))
                                .clickable {
                                    current = -1
                                },
                        ) {}
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(),
                            shape = RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                        ) {
                            LazyVerticalGrid(
                                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                columns = GridCells.Fixed(4),
                                userScrollEnabled = false,
                                contentPadding = PaddingValues(
                                    start = 12.dp,
                                    end = 12.dp,
                                    bottom = 12.dp
                                ),
                            ) {
                                items(items) { item ->
                                    Surface(
                                        modifier = Modifier.padding(4.dp),
                                        color = LightGrey,
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(
                                            item,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                items(tagList) { item ->
                    Surface(
                        color = Color.LightGray.copy(0.2f),
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun CanPlay() {
    var selected by remember {
        mutableStateOf(false)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.selectable(
            selected = selected,
            onClick = {
                selected = !selected
            }
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayCircleOutline, contentDescription = "can play",
            modifier = Modifier.size(16.dp),
            tint = if (selected) MaterialTheme.colorScheme.primary else Color.Red,
        )
        Text(
            text = "?????????",
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    }
}

@Composable
fun FilterItem(
    selected: Boolean = false,
    data: FilterData,
    onClick: () -> Unit = {},
) {

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.selectable(
                selected = selected,
                onClick = onClick,
            )
        ) {
            Text(text = data.name, style = MaterialTheme.typography.bodySmall)
            Icon(
                imageVector = if (selected) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun FilterMore() {
    Icon(
        Icons.Outlined.FilterAlt,
        contentDescription = null,
        Modifier.size(16.dp),
    )
}


@Composable
fun SearchMovieList(
    onNavigateToDetail: () -> Unit = {}
) {
    Column {
        searchMovieList.forEach { item ->
            SearchMovieItem(
                onNavigateToDetail = onNavigateToDetail,
                item = item
            )
        }
    }
}

@Composable
fun SearchMovieItem(
    onNavigateToDetail: () -> Unit = {},
    item: MovieItem
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .height(150.dp)
        ) {
            AsyncImage(
                model = item.image,
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onNavigateToDetail),
            )
            Spacer(modifier = Modifier.width(16.dp))
            PhotoPager(item.photos)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "${item.name} (${item.year})",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                com.example.doubanmovie.ui.components.RatingBar(rating = item.rating)
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    maxLines = 1,
                )
            }
            MovieFavorite(favorite = item.favorite)
        }
        Column {
            MovieComment(item.comment)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            MovieTags(tags = item.tags)
        }
    }
}

@Composable
fun MovieFavorite(favorite: Boolean = false) {
    Surface(
        color = Color.Transparent,
        contentColor = if (favorite) Color.Gray else MaterialTheme.colorScheme.primary
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Icon(
                imageVector = if (favorite) Icons.Outlined.Done else Icons.Outlined.FavoriteBorder,
                contentDescription = null
            )
            Text(
                text = if (favorite) "?????????" else "??????",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun MovieTags(tags: List<String>) {
    FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
        tags.forEach { item ->
            Surface(color = LightGrey, shape = MaterialTheme.shapes.small) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item, style = MaterialTheme.typography.bodySmall)
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun MovieComment(comment: MovieComment) {
    val text = buildAnnotatedString {
        append(AnnotatedString(comment.content))
        append(AnnotatedString(" ??? ${comment.user}", spanStyle = SpanStyle(Color.Gray)))
    }
    Text(text = text, style = MaterialTheme.typography.bodySmall)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PhotoPager(photos: List<String>) {
    val pagerState = rememberPagerState()
    Box {
        HorizontalPager(
            count = photos.size,
            state = pagerState,
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) { page ->
            AsyncImage(
                model = photos[page],
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMovieListPreview() {
    DoubanMovieTheme {
        SearchMovieList()
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMovieBodyPreview() {
    DoubanMovieTheme {
        SearchMovieBody()
    }
}

@Preview(showBackground = true)
@Composable
fun TabBarPreview() {
    DoubanMovieTheme {
//        TabBar()
    }
}

//@Preview(showBackground = true, widthDp = 420)
//@Composable
//fun SearchMoviePreview() {
//    DoubanMovieTheme {
//        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//            SearchMovie()
//        }
//    }
//}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun MovieRankPreview() {
    DoubanMovieTheme {
        MovieRank()
    }
}


@Preview(showBackground = true, widthDp = 420)
@Composable
fun ComingMoviePreview() {
    DoubanMovieTheme {
        ComingMovie()
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun HitTheatersPreview() {
    DoubanMovieTheme {
        HitTheaters()
    }
}


@Preview(showBackground = true, widthDp = 420)
@Composable
fun TabListPreview() {
    DoubanMovieTheme {
        NavList()
    }
}


@Preview(showBackground = true, widthDp = 420)
@Composable
fun MovieScreenPreview() {
    DoubanMovieTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen()
        }
    }
}

data class FilterData(val name: String, val items: List<String>)

private val filterList = listOf(
    FilterData(
        "??????",
        listOf(
            "????????????", "??????", "??????", "??????",
            "??????", "??????", "??????", "??????",
            "??????", "??????", "??????", "??????",
            "??????", "??????", "??????", "??????",
            "??????", "??????", "??????", "??????",
            "??????", "?????????", "??????"
        )
    ),
    FilterData(
        "??????",
        listOf(
            "????????????", "??????", "??????", "??????",
            "??????", "????????????", "??????", "????????????",
            "????????????", "??????", "??????", "??????",
            "?????????", "?????????", "??????", "??????",
            "?????????", "?????????", "????????????", "?????????",
            "??????", "??????", "??????"
        )
    ),
    FilterData(
        "??????",
        listOf(
            "????????????", "????????????", "????????????", "????????????",
        )
    ),
)

data class ComingMovieItem(
    val title: String,
    val image: String,
    val desc: String,
)

private val comingMovieList = listOf(
    ComingMovieItem(
        title = "??????????????????",
        image = "https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2880320460.webp",
        desc = "?????????6???\n??????????????????",
    ),
    ComingMovieItem(
        title = "??????????????????",
        image = "https://img9.doubanio.com/view/photo/s_ratio_poster/public/p2881299984.webp",
        desc = "?????????12???\n????????????",
    ),
)

data class MovieRankItem(
    val title: String,
    val image: String,
    val color: Color,
    val items: List<MovieItem>
)

private val movieRankList = listOf(
    MovieRankItem(
        title = "??????????????????",
        image = "https://img1.doubanio.com/view/photo/photo/public/p2560609439.webp",
        color = Color(0xFFA58A76),
        items = listOf(
            MovieItem(
                name = "???????????????",
                image = "https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2553116438.webp",
                rating = 8.6f,
            ),
            MovieItem(
                name = "????????????",
                image = "https://img9.doubanio.com/view/photo/s_ratio_poster/public/p2881176356.webp",
                rating = 7.4f,
            ),
            MovieItem(
                name = "??????????????",
                image = "https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2871244838.webp",
                rating = 7.2f,
            ),
        )
    ),
    MovieRankItem(
        title = "?????????????????????",
        image = "https://img9.doubanio.com/view/photo/photo/public/p2799949526.webp",
        color = Color(0xFF6F6672),
        items = listOf(
            MovieItem(
                name = "???????????? 0",
                image = "https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2708190250.webp",
                rating = 8.2f,
            ),
            MovieItem(
                name = "????????????",
                image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2873950053.webp",
                rating = 7.9f,
            ),
            MovieItem(
                name = "????????????????????????????????????",
                image = "https://img9.doubanio.com/view/photo/s_ratio_poster/public/p2868008846.webp",
                rating = 7.7f,
            ),
        )
    ),
    MovieRankItem(
        title = "???????????? Top250",
        image = "https://img3.doubanio.com/view/photo/photo/public/p456482220.webp",
        color = Color(0xFF32323B),
        items = listOf(
            MovieItem(
                name = "??????????????????",
                image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp",
                rating = 9.7f,
            ),
            MovieItem(
                name = "????????????",
                image = "https://img3.doubanio.com/view/photo/s_ratio_poster/public/p2561716440.webp",
                rating = 9.6f,
            ),
            MovieItem(
                name = "????????????",
                image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2372307693.webp",
                rating = 9.5f,
            ),
        )
    ),
)


data class NavItem(
    val name: String,
    @DrawableRes val icon: Int,
)

private val movieList = listOf(
    MovieItem(
        "????????????",
        "https://img9.doubanio.com/view/photo/m_ratio_poster/public/p2881176356.webp",
        rating = 7.4f,
        favorite = true,
    ),
    MovieItem(
        "?????????????????????",
        "https://img2.doubanio.com/view/photo/m_ratio_poster/public/p2879301401.webp",
        rating = 7.6f
    ),
    MovieItem(
        "????????????",
        "https://img3.doubanio.com/view/photo/m_ratio_poster/public/p2880994870.webp",
        rating = 6.1f
    ),
    MovieItem(
        "??????",
        "https://img3.doubanio.com/view/photo/m_ratio_poster/public/p2881182580.webp",
        rating = null
    ),
    MovieItem(
        "????????????",
        "https://img2.doubanio.com/view/photo/m_ratio_poster/public/p2880026973.webp",
        rating = 5.3f
    ),
    MovieItem(
        "????????????",
        "https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2876409008.webp",
        rating = 6.8f
    ),
    MovieItem(
        "??????",
        "https://img9.doubanio.com/view/photo/m_ratio_poster/public/p2879572474.webp",
        rating = 5.6f
    ),
    MovieItem(
        "????????????",
        "https://img2.doubanio.com/view/photo/m_ratio_poster/public/p2880944813.webp",
        rating = null
    ),
    MovieItem(
        "?????????????????????",
        "https://img1.doubanio.com/view/photo/m_ratio_poster/public/p2877827228.webp",
        rating = 7.2f
    ),
)

private val searchMovieList = listOf(
    MovieItem(
        name = "??????????????????",
        image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp",
        rating = 9.7f,
        favorite = false,
        year = "1994",
        comment = MovieComment(
            content = "????????????????????????????????????????????????????????????????????????????????????",
            user = "?????????"
        ),
        subtitle = "1994 / ?????? / ?????? ?????? / ??????????????????????? / ????????????????? ?????????????????",
        tags = listOf("?????? ?????? ??????", "?????????????????? ???????????? ????????????"),
        photos = listOf(
            "https://img3.doubanio.com/view/photo/m/public/p456482220.webp",
            "https://img3.doubanio.com/view/photo/m/public/p490576110.webp",
            "https://img2.doubanio.com/view/photo/m/public/p825401501.webp",
            "https://img1.doubanio.com/view/photo/m/public/p490577287.webp"
        )
    ),
    MovieItem(
        name = "????????????",
        image = "https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2557573348.webp",
        rating = 9.4f,
        favorite = true,
        year = "2001",
        comment = MovieComment(
            content = "?????????????????????????????????????????????????????????????????????????????????????????????,??????????????????????????????????????????????????????",
            user = "zing"
        ),
        subtitle = "2001 / ?????? / ?????? ?????? ?????? / ????????? / ????????? ????????????",
        tags = listOf("?????? ?????? ??????", "????????????????????? ?????????????????? ????????????"),
        photos = listOf(
            "https://img9.doubanio.com/view/photo/m/public/p2512318945.webp",
            "https://img9.doubanio.com/view/photo/m/public/p2563777035.webp",
            "https://img1.doubanio.com/view/photo/m/public/p2181503417.webp",
            "https://img1.doubanio.com/view/photo/m/public/p2512318939.webp"
        )
    ),
    MovieItem(
        name = "????????????",
        image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2372307693.webp",
        rating = 9.5f,
        favorite = false,
        year = "1994",
        comment = MovieComment(
            content = "???????????????????????????????????????????????????????????????????????????????????????",
            user = "?????????????????????"
        ),
        subtitle = "1994 / ?????? / ?????? ?????? / ??????????????????????? / ????????????????? ??????????????",
        tags = listOf("?????? ?????? ??????", "?????????????????? ???????????? ????????????"),
        photos = listOf(
            "https://img2.doubanio.com/view/photo/m/public/p1484731332.webp",
            "https://img9.doubanio.com/view/photo/m/public/p1484731424.webp",
            "https://img2.doubanio.com/view/photo/m/public/p825974951.webp",
            "https://img1.doubanio.com/view/photo/m/public/p825966139.webp"
        )
    ),
    MovieItem(
        name = "???????????????",
        image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p479682972.webp",
        rating = 9.3f,
        favorite = false,
        year = "1998",
        comment = MovieComment(
            content = "???????????????????????????????????????????????????????????????",
            user = "??????"
        ),
        subtitle = "1998 / ?????? / ?????? ?????? / ?????????????? / ??????????? ??????????????",
        tags = listOf("?????? ?????? ??????", "????????? ???????????? ????????????"),
        photos = listOf(
            "https://img9.doubanio.com/view/photo/m/public/p764864325.webp",
            "https://img2.doubanio.com/view/photo/m/public/p2614864522.webp",
            "https://img1.doubanio.com/view/photo/m/public/p2614740719.webp",
            "https://img2.doubanio.com/view/photo/m/public/p2614740691.webp"
        )
    ),
    MovieItem(
        name = "?????????????????????",
        image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p579729551.webp",
        rating = 9.2f,
        favorite = true,
        year = "2009",
        comment = MovieComment(
            content = "????????????????????????2010???????????????????????? ?????????",
            user = "??????"
        ),
        subtitle = "2009 / ?????? / ?????? ?????? ?????? / ?????????????????????????? / ?????????????? ????????????????????",
        tags = listOf("?????? ?????? ??????", "????????????????????? ??????????????? ????????????"),
        photos = listOf(
            "https://img2.doubanio.com/view/photo/m/public/p2571368413.webp",
            "https://img2.doubanio.com/view/photo/m/public/p1303106712.webp",
            "https://img1.doubanio.com/view/photo/m/public/p579722647.webp",
            "https://img9.doubanio.com/view/photo/m/public/p1303107905.webp"
        )
    ),
    MovieItem(
        name = "???????????????",
        image = "https://img9.doubanio.com/view/photo/s_ratio_poster/public/p2574551676.webp",
        rating = 9.3f,
        favorite = false,
        year = "1998",
        comment = MovieComment(
            content = "?????????????????????????????????????????????????????????????????????????????????",
            user = "Zuschauerin"
        ),
        subtitle = "1998 / ????????? / ?????? ?????? / ??????????????????????? / ?????????????? ?????????????????????????",
        tags = listOf("????????? ?????? ??????", "????????? ?????????????????? ????????????"),
        photos = listOf(
            "https://img2.doubanio.com/view/photo/m/public/p2581352902.webp",
            "https://img1.doubanio.com/view/photo/m/public/p2574724747.webp",
            "https://img3.doubanio.com/view/photo/m/public/p2573277220.webp",
            "https://img2.doubanio.com/view/photo/m/public/p826011731.webp"
        )
    ),
    MovieItem(
        name = "?????????",
        image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2634997853.webp",
        rating = 8.8f,
        favorite = false,
        year = "2009",
        comment = MovieComment(
            content = "????????? ?????? ?????? in 3D",
            user = "Menphis"
        ),
        subtitle = "2009 / ?????? / ?????? ?????? ?????? / ???????????????????? / ????????????????? ????????????????????",
        tags = listOf("?????? ?????? ??????", "?????????????????? ?????????????????? ????????????"),
        photos = listOf(
            "https://img9.doubanio.com/view/photo/m/public/p595308795.webp",
            "https://img9.doubanio.com/view/photo/m/public/p455284965.webp",
            "https://img2.doubanio.com/view/photo/m/public/p595295073.webp",
            "https://img9.doubanio.com/view/photo/m/public/p595258686.webp"
        )
    ),
    MovieItem(
        name = "???????????????",
        image = "https://img2.doubanio.com/view/photo/s_ratio_poster/public/p2160195181.webp",
        rating = 8.5f,
        favorite = false,
        year = "2013",
        comment = MovieComment(
            content = "????????????LIFE?????????????????????????????????????????????????????????????????????",
            user = "????????????"
        ),
        subtitle = "2013 / ?????? ?????? / ?????? ?????? ?????? / ?????????????? / ?????????????? ????????????????????",
        tags = listOf("?????? ?????? ??????"),
        photos = listOf(
            "https://img1.doubanio.com/view/photo/m/public/p2176074878.webp",
            "https://img9.doubanio.com/view/photo/m/public/p2164194895.webp",
            "https://img1.doubanio.com/view/photo/m/public/p2164057058.webp",
            "https://img1.doubanio.com/view/photo/m/public/p2159273167.webp"
        )
    ),
)

@Composable
fun Text1(){
    Text(text = "111")
}