package mobappdev.example.nback_cimpl

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.StateFlow
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameState
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel



// unit means nothing want to convey
// if want to use a navigate part need to put it on the top
@Composable
fun GameScreen(
    vm: GameViewModel,
    navigateBack: () -> Unit,
) {
    val score by vm.score.collectAsState()
    val gameState by vm.gameState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(12.dp),
            fontSize = 36.sp,
            text = "Score = $score"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(12.dp),
            fontSize = 28.sp,
            text = "N = 2"
        )
        Spacer(modifier = Modifier.height(20.dp))
        GridWithHighlight(gameState = gameState)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { vm.checkMatch() },
            colors = ButtonDefaults.buttonColors(Color.Gray),
            shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
            modifier = Modifier.size(120.dp, 40.dp)
        ) {
            Text(
                text = "match",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { navigateBack() },
            colors = ButtonDefaults.buttonColors(Color.Gray),
            shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
            modifier = Modifier.size(120.dp, 40.dp)
        ) {
            Text(
                text = "back",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GridWithHighlight(
    gameState: GameState,
    modifier: Modifier = Modifier,
    gridSize: Int = 3
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (i in 0 until gridSize) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (j in 0 until gridSize) {
                    val squareNumber = i * gridSize + j
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .background(
                                color = if (squareNumber == (gameState.eventValue - 1)) Color.Blue else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
// {} is a lambda function
// nothing inside {} means nothing
fun GameScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface(){
        GameScreen(FakeVM(),{})
        }
}