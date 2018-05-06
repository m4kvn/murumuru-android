package com.m4kvn.murumuru.ui

import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.m4kvn.murumuru.MusicService
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.core.BaseActivity
import com.m4kvn.murumuru.databinding.ActivityMainBinding
import com.m4kvn.murumuru.ext.replaceFragment
import com.m4kvn.murumuru.ui.main.HomeFragment

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private val mediaController: MediaControllerCompat by lazy {
        MediaControllerCompat(this, mediaBrowser.sessionToken)
                .apply { registerCallback(viewModel.mediaControllerCallback) }
    }
    private val mediaBrowser: MediaBrowserCompat by lazy {
        MediaBrowserCompat(this,
                ComponentName(this, MusicService::class.java),
                mediaBrowserConnectionCallback, null)
    }
    private val mediaBrowserConnectionCallback = object :
            MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()

            if (mediaController.playbackState != null
                    && mediaController.playbackState.state
                    == PlaybackStateCompat.STATE_PLAYING) {
                viewModel.mediaControllerCallback
                        .onMetadataChanged(mediaController.metadata)
                viewModel.mediaControllerCallback
                        .onPlaybackStateChanged(mediaController.playbackState)
            }
            mediaBrowser.subscribe(mediaBrowser.root, viewModel.subscriptionCallback)
        }
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, MusicService::class.java))

        viewModel.requestToChangeFragment.observe(this,
                HomeFragment.newInstance(), Observer {
            it?.let { replaceFragment(R.id.container, it.first, it.second) }
        })

        viewModel.isPlaying.observe(this, Observer { mediaItem ->
            Log.d("MainActivity", "isPlaying.observe=$mediaItem")
            mediaItem ?: return@Observer kotlin.run {}
            mediaController.transportControls.playFromUri(
                    mediaItem.description.mediaUri,
                    mediaItem.description.extras)
        })

        mediaBrowser.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaBrowser.disconnect()
    }
}
