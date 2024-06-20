package com.example.catapult.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.catapult.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val themeSong: MediaPlayer = MediaPlayer.create(context, R.raw.theme)

    private val correctAnswerSound: MediaPlayer = MediaPlayer.create(context, R.raw.correct)
    private val incorrectAnswerSound: MediaPlayer = MediaPlayer.create(context, R.raw.wrong)
    private val gameEndSound: MediaPlayer = MediaPlayer.create(context, R.raw.end)

    fun playThemeSong() {
        Handler(Looper.getMainLooper()).postDelayed({
            themeSong.isLooping = true
            themeSong.start()
        }, 2000)
    }

    fun playCorrectAnswerSound() {
        correctAnswerSound.start()
    }

    fun playIncorrectAnswerSound() {
        incorrectAnswerSound.start()
    }

    fun playGameEndSound() {
        gameEndSound.start()
    }

    fun release() {
        correctAnswerSound.release()
        incorrectAnswerSound.release()
        gameEndSound.release()
    }
}