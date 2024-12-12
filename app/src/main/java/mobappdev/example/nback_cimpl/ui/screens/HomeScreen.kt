package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

/**
 * This is the Home screen composable
 *
 * Currently this screen shows the saved highscore
 * It also contains a button which can be used to show that the C-integration works
 * Furthermore it contains two buttons that you can use to start a game
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */

data class MainScreenState(
    val isButtonEnabled: Boolean = true,
    val text: String = ""
)
@Composable
fun HomeScreen(
    vm: GameViewModel,
    navController: NavController
) {
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(32.dp),
                text = "High Score = $highscore",
                style = MaterialTheme.typography.headlineLarge
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (gameState.eventValue != -1) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Current eventValue is: ${gameState.eventValue}",
                            textAlign = TextAlign.Center
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row {
                            Button(
                                onClick = { },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                            Button(
                                onClick = {},
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                            Button(
                                onClick = { /*TODO*/ },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                        }
                        Row {
                            Button(
                                onClick = { /*TODO*/ },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                            Button(
                                onClick = { /*TODO*/ },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                            Button(
                                onClick = { /*TODO*/ },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                        }
                        Row {
                            Button(
                                onClick =  { /*TODO*/ },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                            Button(
                                onClick = { /*TODO*/ },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                            Button(
                                onClick = { /*TODO*/ },
                                colors = buttonColors(Color.Black),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(4.dp)
                            ) {}
                        }
                    }
                    Button(onClick = vm::startGame) {
                        Text(text = "Generate eventValues")
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        navController.navigate(route = "GameScreen")
                        vm.setGameType(GameType.Audio)
                        vm.startGame()
                        scope.launch {
                            //vm.runAudioGame()
                        }
                    },
                    colors = buttonColors(Color.Black),
                    shape = RoundedCornerShape(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sound_on),
                        contentDescription = "Sound",
                        modifier = Modifier
                            .height(64.dp)
                            .aspectRatio(4f / 2.5f)
                    )
                }

                Button(
                    onClick = {
                        navController.navigate(route = "GameScreen")
                        vm.setGameType(GameType.Visual)
                        vm.startGame()
                        scope.launch {
                        }
                    },
                    colors = buttonColors(Color.Black),
                    shape = RoundedCornerShape(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.visual),
                        contentDescription = "Visual",
                        modifier = Modifier
                            .height(64.dp)
                            .aspectRatio(4f / 2.5f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface(){
        HomeScreen(FakeVM(), rememberNavController())
    }
}
