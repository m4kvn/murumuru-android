package com.m4kvn.murumuru.ui

import android.arch.lifecycle.Observer
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.exoplayer2.SimpleExoPlayer
import com.m4kvn.murumuru.MusicService
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.RC_SIGN_IN
import com.m4kvn.murumuru.core.BaseActivity
import com.m4kvn.murumuru.databinding.ActivityMainBinding
import com.m4kvn.murumuru.ui.binder.SectionMediaItemBinder
import com.m4kvn.murumuru.ui.binder.SectionTitleBinder
import com.m4kvn.murumuru.ui.main.MainSection
import com.m4kvn.murumuru.ui.main.MainViewType
import com.m4kvn.murumuru.ui.main.binder.MainLoginButtonBinder
import com.m4kvn.murumuru.ui.main.binder.MainProfileBinder
import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer
    private val adapter = RecyclerBinderAdapter<MainSection, MainViewType>()
    private val mediaController: MediaControllerCompat by lazy {
        MediaControllerCompat(this, mediaBrowser.sessionToken)
                .apply { registerCallback(mediaControllerCallback) }
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
                mediaControllerCallback
                        .onMetadataChanged(mediaController.metadata)
                mediaControllerCallback
                        .onPlaybackStateChanged(mediaController.playbackState)
            }
            mediaBrowser.subscribe(mediaBrowser.root, viewModel.subscriptionCallback)
        }
    }
    private val mediaControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (metadata == null) {
                binding.playerGroup.visibility = View.GONE
                binding.seekBar.visibility = View.GONE
            } else {
                binding.titleTextView.text = metadata
                        .getText(MediaMetadataCompat.METADATA_KEY_TITLE)
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            binding.playerGroup.visibility = View.VISIBLE
            binding.seekBar.visibility = View.VISIBLE

            if (exoPlayer.duration > 0L) {
                binding.seekBar.max = exoPlayer.duration.toInt()
                if (state != null) {
                    binding.seekBar.progress = state.position.toInt().apply {
                        Log.d(TAG, "seekBar.progress=$this" +
                                ", duration=${exoPlayer.duration}")
                    }
                }
            }

            if (state?.state == PlaybackStateCompat.STATE_PLAYING) {
                binding.playImageView.visibility = View.INVISIBLE
                binding.pauseImageView.visibility = View.VISIBLE
            } else {
                binding.playImageView.visibility = View.VISIBLE
                binding.pauseImageView.visibility = View.INVISIBLE
            }
        }
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, MusicService::class.java))
        binding.viewModel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (!mediaBrowser.isConnected) {
                mediaBrowser.connect()
            } else {
                mediaBrowser.unsubscribe(mediaBrowser.root)
                mediaBrowser.subscribe(mediaBrowser.root, viewModel.subscriptionCallback)
            }
        }

        viewModel.loading.observe(this, Observer {
            binding.swipeRefreshLayout.isRefreshing = it ?: false
        })

        viewModel.currentUser.observe(this, Observer {
            if (it == null) {
                adapter.replaceAll(MainSection.PROFILE,
                        MainLoginButtonBinder(this, {
                            startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(listOf(
                                            AuthUI.IdpConfig.EmailBuilder().build(),
                                            AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(), RC_SIGN_IN)
                        }))
            } else {
                adapter.replaceAll(MainSection.PROFILE,
                        MainProfileBinder(this, it, {
                            AuthUI.getInstance().signOut(this)
                                    .addOnCompleteListener { viewModel.logout() }
                        }))
            }
            if (!mediaBrowser.isConnected) {
                mediaBrowser.connect()
            } else {
                mediaBrowser.unsubscribe(mediaBrowser.root)
                mediaBrowser.subscribe(mediaBrowser.root, viewModel.subscriptionCallback)
            }
        })

        adapter.replaceAll(MainSection.TITLE_NEW,
                SectionTitleBinder(this, "新着", MainViewType.SECTION_TITLE))

        viewModel.mediaItems.observe(this, Observer { mediaItems ->
            mediaItems ?: return@Observer
            Log.d(TAG, "mediaItems.observe=$mediaItems")
            adapter.replaceAll(MainSection.SAMPLES, mediaItems.map { mediaItem ->
                SectionMediaItemBinder(this,
                        MainViewType.SECTION_MEDIA_ITEM, mediaItem,
                        onDetailClick = { viewModel.openDetail(mediaItem) },
                        onViewClick = { viewModel.playSample(mediaItem) })
            })
        })

//        viewModel.requestToChangeFragment.observe(this,
//                HomeFragment.newInstance(), Observer {
//            it?.let { replaceFragment(R.id.container, it.first, it.second) }
//        })

        viewModel.isPlaying.observe(this, Observer { mediaItem ->
            mediaItem ?: return@Observer kotlin.run {}
            mediaController.transportControls.playFromUri(
                    mediaItem.description.mediaUri,
                    mediaItem.description.extras)
        })

        binding.playImageView.setOnClickListener {
            mediaController.transportControls.play()
        }

        binding.pauseImageView.setOnClickListener {
            mediaController.transportControls.pause()
        }

        binding.nextImageView.setOnClickListener {
            mediaController.transportControls.skipToNext()
        }

        binding.previousImageView.setOnClickListener {
            mediaController.transportControls.skipToPrevious()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaController.transportControls
                        .seekTo(seekBar?.progress?.toLong() ?: return)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaBrowser.disconnect()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult(" +
                "requestCode=$requestCode" +
                ", resultCode=$requestCode" +
                ", data=$data)")
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                Log.d("MainActivity", "login successful")
                viewModel.login()
            } else {
                if (response == null) {
                    Snackbar.make(binding.root, "Login Cancel", Snackbar.LENGTH_LONG).show()
                    return
                }
                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(binding.root, "Login Cancel No Internet Connection", Snackbar.LENGTH_LONG).show()
                    return
                }
                Log.e("MainActivity", "Sign-in error: ", response.error)
            }
        }
    }
}
