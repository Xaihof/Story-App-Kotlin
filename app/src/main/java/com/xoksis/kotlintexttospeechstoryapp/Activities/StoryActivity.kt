package com.xoksis.kotlintexttospeechstoryapp.Activities

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xoksis.kotlintexttospeechstoryapp.Constants
import com.xoksis.kotlintexttospeechstoryapp.Data_Classes.Story
import com.xoksis.kotlintexttospeechstoryapp.R
import com.xoksis.kotlintexttospeechstoryapp.databinding.ActivityStoryBinding
import java.util.Locale

class StoryActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    var storyBinding: ActivityStoryBinding? = null
    private var position = 0
    private lateinit var storyList: ArrayList<Story>
    private lateinit var speakableText: String
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding?.root)

        position = intent.getIntExtra("position", 0)
        storyList = Constants.getStoryList()
        tts = TextToSpeech(this, this)
        setStoryView()
        setSpeakableText()
        storyBinding?.btnNext?.setOnClickListener {

            if (position < storyList.size - 1) {
                onChangeStory(1)
            } else {
                Toast.makeText(this, "No more stories available", Toast.LENGTH_SHORT).show()
            }
        }
        storyBinding?.btnPrevious?.setOnClickListener {

            if (position > 0) {

                onChangeStory(-1)
            } else {
                Toast.makeText(this, "No more stories available", Toast.LENGTH_SHORT).show()
            }
        }
        storyBinding?.btnPlay?.setOnClickListener { playStory() }
    }

    private fun playStory() {

        if (!isPlaying) {
            isPlaying = true
            speakOut(speakableText)
            storyBinding?.btnPlay?.setImageResource(R.drawable.pause)
        } else {
            tts?.stop()
            isPlaying = false
            storyBinding?.btnPlay?.setImageResource(R.drawable.play)
        }

    }

    private fun onChangeStory(offStory: Int) {
        tts?.stop()
        position += offStory
        isPlaying = false
        setStoryView()
        storyBinding?.btnPlay?.setImageResource(R.drawable.play)
        setSpeakableText()
    }

    private fun setStoryView() {
        val story = storyList[position]
        storyBinding?.storyImage?.setImageResource(story.image2)
        storyBinding?.tvStoryTitle?.setText(story.title)
        storyBinding?.tvStory?.setText(story.story)
        storyBinding?.tvMoral?.setText(story.moral)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The specified lang is not supported!")
            } else
                Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setSpeakableText() {
        speakableText =
            getString(storyList[position].story) + "Moral of the story. " + getString(storyList[position].moral)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        storyBinding = null
    }
}