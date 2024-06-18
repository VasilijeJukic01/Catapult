package com.example.catapult.model.quiz.end_quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.database.entities.LeaderboardData
import com.example.catapult.datastore.UserStore
import com.example.catapult.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.catapult.model.quiz.end_quiz.EndQuizContract.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext

@HiltViewModel
class EndQuizViewModel @Inject constructor(
    private val repository: LeaderboardRepository,
    private val dataStore: UserStore
) : ViewModel() {

    // Event
    private val eventsFlow = MutableSharedFlow<EndQuizUIEvent>()

    fun setEvent(event: EndQuizUIEvent) = viewModelScope.launch { eventsFlow.emit(event) }

    init {
        handleEvents()
    }

    // Events
    @OptIn(FlowPreview::class)
    private fun handleEvents() {
        viewModelScope.launch {
            eventsFlow
                .debounce(300)
                .collect { event ->
                    handleEvent(event)
                }
        }
    }

    private fun handleEvent(event: EndQuizUIEvent) {
        when (event) {
            // Submit
            is EndQuizUIEvent.Submit -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val userData = dataStore.getActiveUser()
                        val leaderboardData = LeaderboardData(
                            nickname = userData.nickname,
                            result = event.result,
                            category = event.category,
                            submitted = event.submitted,
                            createdAt = System.currentTimeMillis()
                        )

                        if (event.submitted == 1) {
                            repository.submitQuizResultAPI(leaderboardData)
                        }
                        else {
                            repository.submitQuizResultDB(leaderboardData)
                        }
                    }
                }
            }
        }
    }

}