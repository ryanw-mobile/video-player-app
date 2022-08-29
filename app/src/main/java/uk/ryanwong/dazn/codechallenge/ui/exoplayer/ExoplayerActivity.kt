/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.exoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import uk.ryanwong.dazn.codechallenge.databinding.ActivityExoplayerBinding

/****
 * A very simple video player using Exoplayer just to play the video without extra customizations
 * Reference: https://developer.android.com/codelabs/exoplayer-intro#2
 *
 * Rotation / instance state handling has not been handled intentionally in this project,
 * but this could be done.
 */
@AndroidEntryPoint
class ExoplayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExoplayerBinding
    private lateinit var videoUrl: String
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExoplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Safe-args helped ensure the videoUrl is available
        val args: ExoplayerActivityArgs by navArgs()
        videoUrl = args.videoUrl

        binding.lifecycleOwner = this
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                binding.playerView.player = exoPlayer
                val mediaItem = MediaItem.fromUri(videoUrl)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }
}
