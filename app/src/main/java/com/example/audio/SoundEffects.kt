package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

object SoundEffects {
    var isMuted: Boolean = false

    private val scope = CoroutineScope(Dispatchers.Default)

    fun playMove() {
        if (isMuted) return
        playTones(listOf(Tone(freq = 440.0, durationMs = 40, volume = 0.25f)))
    }

    fun playRotate() {
        if (isMuted) return
        playTones(
            listOf(
                Tone(freq = 523.25, durationMs = 40, volume = 0.3f),
                Tone(freq = 659.25, durationMs = 50, volume = 0.35f)
            )
        )
    }

    fun playDrop() {
        if (isMuted) return
        playTones(
            listOf(
                Tone(freq = 330.0, durationMs = 45, volume = 0.4f),
                Tone(freq = 220.0, durationMs = 60, volume = 0.5f)
            )
        )
    }

    fun playHold() {
        if (isMuted) return
        playTones(
            listOf(
                Tone(freq = 392.0, durationMs = 50, volume = 0.3f),
                Tone(freq = 587.33, durationMs = 60, volume = 0.35f)
            )
        )
    }

    fun playLineClearJump() {
        if (isMuted) return
        // Cheerful ascending celebratory jump arpeggio!
        playTones(
            listOf(
                Tone(freq = 523.25, durationMs = 70, volume = 0.4f), // C5
                Tone(freq = 659.25, durationMs = 70, volume = 0.45f), // E5
                Tone(freq = 783.99, durationMs = 70, volume = 0.5f), // G5
                Tone(freq = 1046.50, durationMs = 120, volume = 0.6f) // C6
            )
        )
    }

    fun playGameOver() {
        if (isMuted) return
        playTones(
            listOf(
                Tone(freq = 440.0, durationMs = 120, volume = 0.4f),
                Tone(freq = 392.0, durationMs = 140, volume = 0.4f),
                Tone(freq = 349.23, durationMs = 160, volume = 0.45f),
                Tone(freq = 293.66, durationMs = 260, volume = 0.5f)
            )
        )
    }

    private data class Tone(val freq: Double, val durationMs: Int, val volume: Float)

    private fun playTones(tones: List<Tone>) {
        scope.launch {
            try {
                val sampleRate = 22050
                var totalSamples = 0
                for (t in tones) {
                    totalSamples += (sampleRate * (t.durationMs / 1000.0)).toInt()
                }
                val buffer = ShortArray(totalSamples)
                var writeIdx = 0

                for (t in tones) {
                    val count = (sampleRate * (t.durationMs / 1000.0)).toInt()
                    for (i in 0 until count) {
                        val time = i.toDouble() / sampleRate
                        // Soft envelope to avoid clicking
                        val attack = (i.toFloat() / (sampleRate * 0.005f)).coerceIn(0f, 1f)
                        val decay = ((count - i).toFloat() / (sampleRate * 0.01f)).coerceIn(0f, 1f)
                        val env = attack * decay
                        val wave = sin(2.0 * Math.PI * t.freq * time) * t.volume * env
                        buffer[writeIdx++] = (wave * Short.MAX_VALUE).toInt().toShort()
                    }
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                // Release when done
                kotlinx.coroutines.delay(tones.sumOf { it.durationMs.toLong() } + 100L)
                audioTrack.release()
            } catch (_: Exception) {
                // Ignore audio failure gracefully
            }
        }
    }
}
