package mobappdev.example.nback_cimpl

// Import statements for UI components, layouts, and utilities
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import mobappdev.example.nback_cimpl.ui.viewmodels.*

// Composable function to create a grid with highlighted cells
@Composable
fun GridWithHighlight(
    gameState: GameState,              // Contains the index of the highlighted cell
    modifier: Modifier = Modifier,     // Modifier for customizing layout if needed
    gridSize: Int = 3                  // Size of the grid, defaults to 3x3
) {
    Column(
        modifier = modifier.padding(16.dp),                // Padding around the grid
        horizontalAlignment = Alignment.CenterHorizontally, // Centers the grid horizontally
        verticalArrangement = Arrangement.Center           // Centers the grid vertically
    ) {
        for (i in 0 until gridSize) {                       // Outer loop for each row in the grid
            Row(
                horizontalArrangement = Arrangement.Center, // Centers cells within each row
                modifier = Modifier.fillMaxWidth()          // Makes each row take full width
            ) {
                for (j in 0 until gridSize) {               // Inner loop for each cell in the row
                    val squareNumber = i * gridSize + j     // Calculate unique index for each cell
                    Box(
                        modifier = Modifier
                            .size(90.dp)                    // Cell size
                            .padding(6.dp)                  // Padding between cells
                            .background(
                                color = if (squareNumber == (gameState.eventValue - 1)) Color.Red else Color.Gray, // Highlight based on gameState
                                shape = CircleShape         // Circle shape for each cell
                            )
                    )
                }
            }
        }
    }
}

// Main Composable function for the Game Screen
@Composable
fun GameScreen(
    vm: GameViewModel,                // ViewModel for managing game state and interactions
    navigateBack: () -> Unit,         // Function to handle navigation back from the game screen
) {
    val score by vm.score.collectAsState()         // Collect score state
    val gameState by vm.gameState.collectAsState() // Collect game state

    Column(
        modifier = Modifier.fillMaxWidth(),        // Fills the width of the screen
        horizontalAlignment = Alignment.CenterHorizontally // Centers all children horizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))  // Spacer for vertical space

        // Displaying player's score
        Text(
            modifier = Modifier.padding(12.dp),
            fontSize = 36.sp,
            text = "Score = $score"
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Static text for N level
        Text(
            modifier = Modifier.padding(12.dp),
            fontSize = 28.sp,
            text = "N = 2"
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Grid component
        GridWithHighlight(gameState = gameState)
        Spacer(modifier = Modifier.height(20.dp))

        // Button to check for match
        Button(
            onClick = { vm.checkMatch() },
            colors = ButtonDefaults.buttonColors(
                gameState.matchbuttonColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(120.dp, 40.dp)
        ) {
            Text(
                text = "match",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Button to navigate back
        Button(
            onClick = { navigateBack() },
            colors = ButtonDefaults.buttonColors(Color.Black),
            shape = RoundedCornerShape(8.dp),
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

// Preview Composable for ScreenforGame layout visualization
// FakeVM
// Purpose of FakeVM: In Composable previews or tests, where we want to visualize or test GameScreen in isolation, we use FakeVM instead of GameVM.
// FakeVM provides mock data and behavior without needing the full GameVM setup.
// This makes it useful for testing the UI in isolation without relying on actual data or the Android lifecycle.
@Preview
@Composable
fun GameScreenPreview() {
    Surface {
        GameScreen(FakeVM(), {}) // Preview using FakeVM
    }
}
