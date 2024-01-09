package com.music.player.presentation.musicPlayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.music.player.R
import com.music.player.data.model.FavouriteModel
import com.music.player.domain.use_case.welcomeMusicList.FavouriteViewModel
import com.music.player.domain.use_case.welcomeMusicList.WelcomePageViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//Activity for DisplayingMusic Player
@OptIn(DelicateCoroutinesApi::class)
class MusicPlayer : AppCompatActivity() {
    private lateinit var viewmodel: FavouriteViewModel
    private var selectedMusicItem: FavouriteModel?=null
    private lateinit var musicTracks:List<FavouriteModel>
    private lateinit var forward: ImageView
    private lateinit var rewind: ImageView
    private lateinit var start: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var countdownTextView: TextView
    private val updateHandler = Handler()
    private var isCountdownRunning = false
    private lateinit var welcomePageViewModel: WelcomePageViewModel

    private var isFavorite: Boolean = false

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_player)

        supportActionBar?.title="Music Player"

        val intent=intent
        selectedMusicItem=intent.getParcelableExtra("Music_list")
        //setting up Media Player
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(selectedMusicItem?.filepath.toString())
        mediaPlayer.prepare()
        mediaPlayer.start()

        welcomePageViewModel= ViewModelProvider(this).get(WelcomePageViewModel::class.java)


        musicTracks = welcomePageViewModel.getAudioTrack(this)
        viewmodel= ViewModelProvider(this).get(FavouriteViewModel::class.java)

        countdownTextView = findViewById(R.id.decresetime)
        val minutes=mediaPlayer.duration/1000/60
        val seconds=(mediaPlayer.duration/1000)%60
        val currentTime=String.format("%02d:%02d",minutes,seconds)
        findViewById<TextView>(R.id.totaltime).text=currentTime
        findViewById<TextView>(R.id.musictitle).text=selectedMusicItem?.title.toString()

        seekBar = findViewById(R.id.seekbar)
        seekBar.max = mediaPlayer.duration

        mediaPlayer.setOnCompletionListener {
            stopCountdown()
            seekBar.progress = 0
        }

        //changing the playing music progress on seekbar touch
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        startCountdown()
        setupToolbar()
        setupMusicControls()

        viewmodel.getFavourite().observe(this, Observer {
            isFavorite=it.any{
                it.title==selectedMusicItem?.title.toString() && it.artist==selectedMusicItem?.artist.toString()
            }
        })

    }

    private fun startCountdown() {
        if (!isCountdownRunning) {
            isCountdownRunning = true
            updateHandler.postDelayed(updateRunnable, 1000)
        }
    }

    private fun stopCountdown() {
        if (isCountdownRunning) {
            isCountdownRunning = false
            updateHandler.removeCallbacks(updateRunnable)
        }
    }

    private val updateRunnable = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            if (mediaPlayer.isPlaying) {
                val currentPosition = mediaPlayer.currentPosition
                seekBar.progress = currentPosition
                val minutes=currentPosition/1000/60
                val seconds=(currentPosition/1000)%60
                val currenttime=String.format("%02d:%02d",minutes,seconds)
                countdownTextView.text = currenttime
                updateHandler.postDelayed(this, 1000)
            } else {
                stopCountdown()
            }
        }
    }

    private fun setupMusicControls() {
        start=findViewById<ImageView>(R.id.play)
        start.setOnClickListener {
            if (mediaPlayer.isPlaying){
                pauseMusicPlayback()
            }else{
                startMusicPlayback()
            }
        }
        rewind=findViewById<ImageView>(R.id.rewind)
        rewind.setOnClickListener {
            moveBackward()
        }
        forward=findViewById<ImageView>(R.id.forword)
        forward.setOnClickListener {
            moveForward()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.title="Music Player"
    }

    //start playing music
    private fun startMusicPlayback() {
        mediaPlayer.start()
        startCountdown()
        start.setImageResource(R.drawable.stop)
    }
    private fun moveForward() {
        val currentSongIndex = musicTracks.indexOf(selectedMusicItem)
        if (currentSongIndex < musicTracks.size - 1) {
            val nextMusicItem = musicTracks[currentSongIndex + 1]
            mediaPlayer.reset()
            mediaPlayer.setDataSource(nextMusicItem.filepath)
            mediaPlayer.prepare()
            startMusicPlayback()
            selectedMusicItem = nextMusicItem
            seekBar.progress=0
            val minutes=nextMusicItem.duration/1000/60
            val seconds=(nextMusicItem.duration/1000)%60
            val currenttime=String.format("%02d:%02d",minutes,seconds)
            findViewById<TextView>(R.id.totaltime).text=currenttime
            findViewById<TextView>(R.id.musictitle).text=nextMusicItem?.title.toString()
        } else {
            Toast.makeText(this, "No more songs to play", Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Song List")
            builder.setMessage("No more songs to play")
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                builder.create().cancel()
            }
            builder.setNegativeButton("No"){dialogInterface, which ->
                builder.create().cancel()
            }
            builder.create()
            builder.show()
        }
    }

    //pause music
    private fun pauseMusicPlayback() {
        mediaPlayer.pause()
        stopCountdown()
        start.setImageResource(R.drawable.play)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null){
            mediaPlayer.stop()
        }
    }

    //setup menu in support action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.musicplayer_menu,menu)
        var fvarate=menu!!.findItem(R.id.favorite)
        if (isFavorite) {
            fvarate.setIcon(R.drawable.favourite)
        }else{
            fvarate.setIcon(R.drawable.favorite_unfilled)
        }
        return super.onCreateOptionsMenu(menu)
    }

    //inserting and deleting from favourite table on clicking of favourite icon on music player page
    @OptIn(DelicateCoroutinesApi::class)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite->{
                val icon=if (isFavorite) R.drawable.favourite else R.drawable.favorite_unfilled
                item.setIcon(icon)
                true
                GlobalScope.launch {
                    if (isFavorite) {
                        viewmodel.delete(
                            FavouriteModel(
                                selectedMusicItem?.Id.toString().toLong(),
                                selectedMusicItem?.title.toString(),
                                selectedMusicItem?.artist.toString(),
                                selectedMusicItem?.album.toString(),
                                selectedMusicItem?.duration.toString().toLong(),
                                selectedMusicItem?.filepath.toString()
                            )
                        )
                    }else{
                        viewmodel.insert(
                            FavouriteModel(
                                selectedMusicItem?.Id.toString().toLong(),
                                selectedMusicItem?.title.toString(),
                                selectedMusicItem?.artist.toString(),
                                selectedMusicItem?.album.toString(),
                                selectedMusicItem?.duration.toString().toLong(),
                                selectedMusicItem?.filepath.toString()
                            )
                        )

                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveBackward() {
        val currentSongIndex = musicTracks.indexOf(selectedMusicItem)
        if (currentSongIndex > 0) {
            val previousMusicItem = musicTracks[currentSongIndex - 1]
            mediaPlayer.reset()
            mediaPlayer.setDataSource(previousMusicItem.filepath)
            mediaPlayer.prepare()
            startMusicPlayback()
            selectedMusicItem = previousMusicItem
            seekBar.progress=0
            val minutes=previousMusicItem.duration/1000/60
            val seconds=(previousMusicItem.duration/1000)%60
            val currenttime=String.format("%02d:%02d",minutes,seconds)
            findViewById<TextView>(R.id.totaltime).text=currenttime
            findViewById<TextView>(R.id.musictitle).text=previousMusicItem?.title.toString()

        } else {
            Toast.makeText(this, "No previous songs", Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Song List")
            builder.setMessage("No previous songs")
            builder.setPositiveButton("Yes"){dialogInterface, which ->
                builder.create().cancel()
            }
            builder.setNegativeButton("No"){dialogInterface, which ->
                builder.create().cancel()
            }
            builder.create()
            builder.show()
        }
    }
}