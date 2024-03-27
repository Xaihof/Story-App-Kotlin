package com.xoksis.kotlintexttospeechstoryapp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xoksis.kotlintexttospeechstoryapp.Adapter_Classes.StoryAdapter
import com.xoksis.kotlintexttospeechstoryapp.Constants
import com.xoksis.kotlintexttospeechstoryapp.Data_Classes.Story
import com.xoksis.kotlintexttospeechstoryapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Constants.getStoryList()
        setAdapterRecyclerView(data)

    }

    private fun setAdapterRecyclerView(data: ArrayList<Story>) {

        val recyclerView = findViewById<RecyclerView>(R.id.rvStoryList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = StoryAdapter(data)
        recyclerView.adapter = adapter

        adapter.setOnClickListener(object : StoryAdapter.OnClickListener {
            override fun onClick(position: Int) {

                val intent = Intent(this@MainActivity, StoryActivity::class.java)
                intent.putExtra("position", position)
                startActivity(intent)

            }

        })

    }
}