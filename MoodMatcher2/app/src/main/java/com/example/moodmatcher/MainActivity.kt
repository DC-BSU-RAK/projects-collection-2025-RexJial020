package com.example.moodmatcher

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.example.moodmatcher.ui.theme.MoodMatcherTheme
import kotlinx.coroutines.delay
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

// Song Data Class
data class Song(val title: String, val artist: String, val coverUrl: String)

val moodSongs = mapOf(
    "happy" to listOf(
        Song("Happy", "Pharrell Williams", "https://i.scdn.co/image/ab67616d0000b273d92f4e1639c8e1d4e666f5ef"),
        Song("Can't Stop the Feeling!", "Justin Timberlake", "https://i.scdn.co/image/ab67616d0000b2736d1e41d7d8e35a2098e9f1a5")
    ),
    "sad" to listOf(
        Song("Someone Like You", "Adele", "https://i.scdn.co/image/ab67616d0000b27396e48de56427177d3dca4982")
    ),
    "angry" to listOf(
        Song("Break Stuff", "Limp Bizkit", "https://i.scdn.co/image/ab67616d0000b2738ec1d9c0b71bdbb72e62e354")
    ),
    "love" to listOf(
        Song("Love Story", "Taylor Swift", "https://i.scdn.co/image/ab67616d0000b2732ecbb8ccf2aa839d7dbccacf")
    ),
    "sleepy" to listOf(
        Song("Weightless", "Marconi Union", "https://i.scdn.co/image/ab67616d0000b273b83dddf3a6f6f12d3c9bb2fa")
    )
)

@Composable
fun MoodAnimation(moodKey: String) {
    val animationRes = when (moodKey) {
        "happy" -> R.raw.happy_anim
        "sad" -> R.raw.sad_anim
        "angry" -> R.raw.angry_anim
        "love" -> R.raw.love_anim
        "sleepy" -> R.raw.sleepy_anim
        else -> null
    }

    animationRes?.let {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(it))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun MoodSongRecommendation(moodKey: String) {
    val songs = moodSongs[moodKey] ?: return
    var song by remember { mutableStateOf(songs.random()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(song.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(song.artist, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = song.coverUrl,
            contentDescription = "Cover art of ${song.title}",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { song = songs.random() }) {
            Text("Shuffle 🔁")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodMatcherScreen() {
    val context = LocalContext.current
    var red by remember { mutableStateOf(255) }
    var green by remember { mutableStateOf(0) }
    var blue by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            red = (0..255).random()
            green = (0..255).random()
            blue = (0..255).random()
            delay(1000)
        }
    }

    val targetColor = Color(red, green, blue)
    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(1000),
        label = "BackgroundColor"
    )

    var moodMessage by remember { mutableStateOf("") }
    var showMoodDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var currentMoodKey by remember { mutableStateOf("") }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    DisposableEffect(mediaPlayer) {
        onDispose { mediaPlayer?.release() }
    }

    val spotifyLinks = mapOf(
        "happy" to "https://open.spotify.com/playlist/37i9dQZF1DXdPec7aLTmlC",
        "sad" to "https://open.spotify.com/playlist/37i9dQZF1DX7qK8ma5wgG1",
        "angry" to "https://open.spotify.com/playlist/37i9dQZF1DWUvQoIOFMFUT",
        "love" to "https://open.spotify.com/playlist/37i9dQZF1DX50QitC6Oqtn",
        "sleepy" to "https://open.spotify.com/playlist/37i9dQZF1DWUZ5bk6qqDSy"
    )

    val spotifyNames = mapOf(
        "happy" to "Happy Vibes Playlist",
        "sad" to "Sad Songs Playlist",
        "angry" to "Angry Beats Playlist",
        "love" to "Love Songs Playlist",
        "sleepy" to "Chill & Sleep Playlist"
    )

    val moods = listOf(
        "😊" to Pair("happy", "You’re feeling joyful! Here’s a happy tune. 🎵"),
        "😢" to Pair("sad", "Feeling down? This might cheer you up. 🎶"),
        "😠" to Pair("angry", "Let it out with this intense beat. 💥"),
        "😍" to Pair("love", "Love is in the air… Enjoy this vibe. ❤️"),
        "😴" to Pair("sleepy", "Relax and drift off… 😴")
    )

    Scaffold(
        containerColor = animatedColor,
        topBar = {
            TopAppBar(
                title = { Text("MoodMatcher 🎧", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                actions = {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(Icons.Filled.Info, contentDescription = "App Info")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("How are you feeling today?", fontSize = 20.sp)

                // Grid of emojis (3 columns)
                val chunkedMoods = moods.chunked(3)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    chunkedMoods.forEach { rowMoods ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowMoods.forEach { (emoji, data) ->
                                Surface(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clickable {
                                            mediaPlayer?.release()
                                            moodMessage = data.second
                                            currentMoodKey = data.first
                                            showMoodDialog = true

                                            val resId = when (data.first) {
                                                "happy" -> R.raw.happy
                                                "sad" -> R.raw.sad
                                                "angry" -> R.raw.angry
                                                "love" -> R.raw.love
                                                "sleepy" -> R.raw.sleepy
                                                else -> R.raw.happy
                                            }

                                            mediaPlayer = MediaPlayer.create(context, resId)
                                            mediaPlayer?.start()
                                        },
                                    shape = RoundedCornerShape(50),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(emoji, fontSize = 28.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Mood Info Dialog (shows on emoji tap)
            if (showMoodDialog) {
                AlertDialog(
                    onDismissRequest = { showMoodDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showMoodDialog = false }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Mood Detected") },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(moodMessage)
                            MoodAnimation(currentMoodKey)
                            MoodSongRecommendation(currentMoodKey)

                            spotifyNames[currentMoodKey]?.let {
                                Text(it, fontWeight = FontWeight.Bold)
                            }

                            spotifyLinks[currentMoodKey]?.let { url ->
                                Button(onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.data = Uri.parse(url)
                                    context.startActivity(intent)
                                }) {
                                    Text("Open Spotify Recommendation")
                                }
                            }
                        }
                    }
                )
            }

            // App Instructions Dialog (opens via info button)
            if (showInfoDialog) {
                AlertDialog(
                    onDismissRequest = { showInfoDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showInfoDialog = false }) {
                            Text("Close")
                        }
                    },
                    title = { Text("App Instructions") },
                    text = {
                        Text(
                            "Select an emoji that best matches your mood. " +
                                    "The app will play a related sound, show an animation, " +
                                    "and suggest songs for your mood. Tap the emojis to explore!"
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MoodMatcherTheme {
                MoodMatcherScreen()
            }
        }
    }
}
