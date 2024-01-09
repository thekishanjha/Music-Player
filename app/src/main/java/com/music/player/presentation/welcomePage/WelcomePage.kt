package com.music.player.presentation.welcomePage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.music.player.R
import com.music.player.data.model.FavouriteModel
import com.music.player.domain.use_case.welcomeMusicList.WelcomePageViewModel
import com.music.player.presentation.components.MusicAdapter
import com.music.player.presentation.savedPlayList.FavouriteList

//Activity for  Welcome Page
@Suppress("KotlinConstantConditions")
class WelcomePage : AppCompatActivity() {
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var audioTracks: List<FavouriteModel>
    private lateinit var viewModel: WelcomePageViewModel

    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 1001
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)

        supportActionBar?.title="Library"
        supportActionBar?.setIcon(R.drawable.menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this,R.drawable.baseline_menu_24))

        //on starting music player app show all audios from local by checking permission
        if (checkStorageAccess()) {
            showAllAudios()
        } else {
            getStoragePermission()
        }
    }

    //setting menu on welcome page
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.welcomepage_menu,menu)
        var menuItem=menu!!.findItem(R.id.searchicon)
        var searchView: SearchView =menuItem.actionView as SearchView
        val searchIcon =
            searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.WHITE)
        searchView.maxWidth= Int.MAX_VALUE
        //Doing search action
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                musicAdapter.filter(newText.toString())
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    //check if storage permission is granted or not
    private fun checkStorageAccess(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    //Getting Storage permission from user
    private fun getStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    //navigating to favourite page when clicking on favourite item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favourite->{
                item.setIcon(R.drawable.favourite);
                val intent= Intent(this, FavouriteList::class.java)
                startActivity(intent)
            }
        }
        when(item.itemId){
            R.id.item->{
                Toast.makeText(this,"Clicked on 3 dots",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //show all audio files if permission granted otherwise show message for access denied
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAllAudios()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Cannot access audio files without storage permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //showing all audio files from local and showing in recycler view
    private fun showAllAudios() {
        viewModel= ViewModelProvider(this).get(WelcomePageViewModel::class.java)
        audioTracks = viewModel.getAudioTrack(this)

        val recyclerViewMusic = findViewById<RecyclerView>(R.id.reclycleviewmusic)
        musicAdapter = MusicAdapter(audioTracks)
        recyclerViewMusic.adapter = musicAdapter
        recyclerViewMusic.layoutManager = LinearLayoutManager(this)

    }

}