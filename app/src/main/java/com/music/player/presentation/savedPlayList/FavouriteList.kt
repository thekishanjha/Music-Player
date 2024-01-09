package com.music.player.presentation.savedPlayList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.music.player.R
import com.music.player.data.model.FavouriteModel
import com.music.player.domain.use_case.welcomeMusicList.FavouriteViewModel
import com.music.player.presentation.components.FavouriteListAdapater
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//Activity for showing saved playlist

class FavouriteList : AppCompatActivity() {
    private lateinit var favouriteList: List<FavouriteModel>
    private lateinit var viewModel:FavouriteViewModel
    private lateinit var noItemsTextView: TextView
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_playlist)

        supportActionBar?.title="PlayLists"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this,R.drawable.baseline_menu_24))

        viewModel= ViewModelProvider(this).get(FavouriteViewModel::class.java)
        GlobalScope.launch {
            favouriteList=viewModel.getData()
            noItemsTextView=findViewById(R.id.noItemsTextView)
            recyclerView=findViewById<RecyclerView>(R.id.recycleplaylit)
            val adapter = FavouriteListAdapater(favouriteList)
            recyclerView.adapter = adapter
            Log.d("tag","$favouriteList")
            toggleListVisibility()
        }
    }

    private fun toggleListVisibility() {
        if (favouriteList.isEmpty()) {
            recyclerView.visibility = View.GONE
            noItemsTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
        }
    }
}