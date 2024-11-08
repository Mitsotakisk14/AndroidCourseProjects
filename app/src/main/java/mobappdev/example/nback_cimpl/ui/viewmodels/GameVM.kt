package mobappdev.example.nback_cimpl.ui.viewmodels

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository
import java.security.KeyStore.TrustedCertificateEntry
import java.util.Locale
import kotlin.random.Random



/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int

    fun setGameType(gameType: GameType)
    fun startGame()

    fun checkMatch()
}

class GameVM(
    application: GameApplication,
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    // nBack is currently hardcoded
    override val nBack: Int = 1

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events

    // Text to speech part
    // textToSpeech is a variable so is the same code with the job

    private var textToSpeech: TextToSpeech? = null
    private var context = application.applicationContext


    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop

        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(10, 9, 30, nBack).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame(events)
                GameType.AudioVisual -> {}
                GameType.Visual -> runVisualGame(events)
            }
            // Todo: update the highscore
            // update in the value 2 times, first time is in the highscore
            // second time is to use the saveHighscore in the UserPreferences

            if (_score.value > _highscore.value) {
                _highscore.update{ _score.value }
                viewModelScope.launch {
                    userPreferencesRepository.saveHighScore(_score.value)
                }

            }

        }
    }

    override fun checkMatch() {
        // Match means in the N-back game, the sound matches or the location matches
        /**
         * Todo: This function should check if there is a match when the user presses a match button
         * Make sure the user can only register a match once for each event.
         */

        // Match means the current value is equal to the N-back value no matter it is audio or visual match

        // events will create a list array contains 10 elements inside
        // eventIndex and eventValue is already in the for loop
        // If it matched, value + 1
        // If it is not matched, value - 1

        val currentIndex = _gameState.value.eventIndex
        val currentValue = _gameState.value.eventValue
        // val currentState = _gameState.value.eventState

        if (currentIndex >= nBack){
            if ( currentValue == events[currentIndex - nBack]) {
                _score.value++
                //_gameState.value = currentState.copy(isMatch = isMatch)
            }
            else{
                _score.value--
            }
        }

    }

    // map means the list of the sound

    var map = mutableListOf("a", "b", "y", "z", "x", "l", "m", "c", "h", "s")
    private suspend fun runAudioGame(events: Array<Int>) {
        // Todo: Make work for Basic grade

        // When every time start the game, set the game value to 0
        _score.value = 0

        if (textToSpeech == null){
            textToSpeech = TextToSpeech(context) {status ->
                if (status == TextToSpeech.ERROR){
                    Log.e("GameVM", "TextToSpeech not working")
                    return@TextToSpeech
                }

            }
        }


        for ((index, value) in events.withIndex()){
            // Update gameState
            _gameState.value = _gameState.value.copy(
                eventIndex = index,
                eventValue = value,
                //isMatch = null
            )
            textToSpeech?.speak(map[value], TextToSpeech.QUEUE_FLUSH, null, null)
            delay(eventInterval)
        }

    }

    // add index and eventIndex to get the index in the events
    // eventValue and eventIndex are already in the for loop

    private suspend fun runVisualGame(events: Array<Int>){
        // Todo: Replace this code for actual game code

        // When every time start the game, set the game value to 0
        _score.value = 0

        // With index

        for ((index, value) in events.withIndex()) {
            // Update gameState
            _gameState.value = _gameState.value.copy(
                eventIndex = index,
                eventValue = value
            )

            // eventvalue means the square color?

            delay(eventInterval - 500)
            _gameState.value =
                _gameState.value.copy(eventValue = -1)
            // Take away the square, make sure the box is not in the same color for 4 seconds
            // It makes sure in this situation, theses is a time there is no square so we know there is a blink
            delay(500)
        }


    }

    val letters = mutableListOf("a","b","c","d","e","f","g","h","i", )


    //private suspend fun runAudioVisualGame(events:Array<Int>){
    // if (textToSpeech) == null) {
    //  textToSpeech = textToSpeech(context) { status ->
    //    if (statu == textToSpeech.ERROR){
    //      Log.e("GameVM", "TextToSpeech not working")
    //    return@textToSpeech
    //      }
    //}
    // Todo: Make work for Higher grade


    //}



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application, application.userPreferencesRespository)

            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }
}

// Class with the different game types
enum class GameType{
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.Visual,  // Type of the game
    val eventValue: Int = -1,  // The value of the array string
    val eventIndex: Int = 0  // The index of the array string
    // val eventState: GameState
    // val isMatch: Boolean = null
)

class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() = 2

    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkMatch(){
    }
}

