package com.m4kvn.murumuru

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.firebase.firestore.Query
import com.m4kvn.murumuru.ext.*
import com.m4kvn.murumuru.model.SampleMusic
import com.m4kvn.murumuru.repository.FirebaseFirestoreRepository
import com.m4kvn.murumuru.ui.MainActivity
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject
import android.support.v4.media.app.NotificationCompat as NotificationMediaCompat

class MusicService : MediaBrowserServiceCompat() {

    companion object {
        val TAG: String = MusicService::class.java.simpleName
        const val ROOT_ID = "root"
    }

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer
    @Inject
    lateinit var dataSourceFactory: DataSource.Factory
    @Inject
    lateinit var firestoreRepository: FirebaseFirestoreRepository

    private val mediaSession: MediaSessionCompat by lazy {
        MediaSessionCompat(applicationContext, TAG)
    }
    private val audioManager: AudioManager by lazy {
        getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val items: MutableMap<Int, MediaItem> = mutableMapOf()
    private var playingItem: MediaItem? = null

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)

        mediaSession.apply {
            this@MusicService.sessionToken = sessionToken

            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            setCallback(object : MediaSessionCompat.Callback() {

                override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
                    exoPlayer.prepare(ExtractorMediaSource(uri, dataSourceFactory,
                            DefaultExtractorsFactory(), null, null))
                    playingItem = items[extras?.getInt(MEDIA_ITEM_INDEX)]?.apply {
                        setMetadata(MediaMetadataCompat.Builder()
                                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                                        description.mediaId)
                                .putText(MediaMetadataCompat.METADATA_KEY_TITLE,
                                        description.title)
                                .build())
                    }
                    isActive = true
                    onPlay()
                }

                override fun onPlay() {
                    isActive = true
                    exoPlayer.playWhenReady = true
                }

                override fun onPause() {
                    exoPlayer.playWhenReady = false
                }

                override fun onStop() {
                    onPause()
                    isActive = false
                }

                override fun onSeekTo(pos: Long) {
                    exoPlayer.seekTo(pos)
                }

                override fun onSkipToNext() {
                    playingItem?.let {
                        it.description.extras?.getInt(MEDIA_ITEM_INDEX)?.let {
                            items[if (it + 1 >= items.size) 0 else it + 1]?.let {
                                onPlayFromUri(it.description.mediaUri, it.description.extras)
                            }
                        }
                    }
                }

                override fun onSkipToPrevious() {
                    playingItem?.let {
                        it.description.extras?.getInt(MEDIA_ITEM_INDEX)?.let {
                            items[if (it - 1 < 0) items.size - 1 else it - 1]?.let {
                                onPlayFromUri(it.description.mediaUri, it.description.extras)
                            }
                        }
                    }
                }
            })

            controller.registerCallback(object : MediaControllerCompat.Callback() {
                override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                    createNotification()
                }

                override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                    createNotification()
                }
            })
        }

        exoPlayer.addListener(object : ExoPlayer.EventListener {
            override fun onTracksChanged(trackGroups: TrackGroupArray?,
                                         trackSelections: TrackSelectionArray?) {
                Log.d(TAG, "onTracksChanged(" +
                        "trackGroups=$trackGroups, " +
                        "trackSelections=$trackSelections)")
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                Log.d(TAG, "onPlayerError(" +
                        "error=$error)")
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                Log.d(TAG, "onPlayerStateChanged(" +
                        "playerWhenReady=$playWhenReady," +
                        "playbackState=$playbackState)")
                updatePlaybackState(playWhenReady, playbackState)
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                Log.d(TAG, "onLoadingChanged(" +
                        "isLoading=$isLoading)")
            }

            override fun onPositionDiscontinuity() {
                Log.d(TAG, "onPositionDiscontinuity()")
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
                Log.d(TAG, "onTimelineChanged(" +
                        "timeline=$timeline," +
                        "manifest=$manifest)")
            }
        })
    }

    override fun onLoadChildren(
            parentId: String,
            result: Result<MutableList<MediaItem>>) {
        if (parentId == ROOT_ID) {
            result.detach()
            firestoreRepository.sampleMusicCollectionRef
                    .orderBy("upload_time", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener {
                        it.result.documents
                                .filter { it.data != null }
                                .map { SampleMusic.fromFireStore(it.data!!) }
                                .apply { items.clear() }
                                .forEachIndexed { i, sampleMusic ->
                                    items[i] = sampleMusic.toMediaItem(UUID.randomUUID(), i)
                                }
                        result.sendResult(mutableListOf<MediaItem>()
                                .apply { items.forEach { add(it.value) } })
                    }
        } else {
            result.sendResult(ArrayList())
        }
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?) =
            BrowserRoot(ROOT_ID, null)

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.isActive = false
        mediaSession.release()
        exoPlayer.stop()
        exoPlayer.release()
    }

    private fun updatePlaybackState(playWhenReady: Boolean, playbackState: Int) {
        val state = when (playbackState) {
            ExoPlayer.STATE_IDLE -> PlaybackStateCompat.STATE_NONE
            ExoPlayer.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
            ExoPlayer.STATE_READY -> if (playWhenReady)
                PlaybackStateCompat.STATE_PLAYING else
                PlaybackStateCompat.STATE_PAUSED
            ExoPlayer.STATE_ENDED -> PlaybackStateCompat.STATE_STOPPED
            else -> PlaybackStateCompat.STATE_NONE
        }
        mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        or PlaybackStateCompat.ACTION_STOP)
                .setState(state, exoPlayer.currentPosition, 1.0f)
                .build())
    }

    private fun createNotification() {
        if (mediaSession.controller.metadata == null
                && !mediaSession.isActive) return

        val description =  playingItem?.description ?: return

        startForeground(1, NotificationCompat
                .Builder(applicationContext, "murumuru")
                .applySetContentTitle(description.title)
                .applySetContentIntent(PendingIntent.getActivity(this, 1,
                        Intent(this, MainActivity::class.java)
                                .apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP },
                        PendingIntent.FLAG_CANCEL_CURRENT))
                .applySetDeleteIntent(MediaButtonReceiver
                        .buildMediaButtonPendingIntent(this,
                                PlaybackStateCompat.ACTION_STOP))
                .applySetVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .applySetSmallIcon(R.drawable.exo_controls_play)
                .applySetColor(ContextCompat.getColor(this,
                        R.color.colorAccent))
                .applySetStyle(NotificationMediaCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken)
                        .setShowActionsInCompactView(1))
                .applyAddAction(NotificationCompat.Action(
                        R.drawable.exo_controls_previous, "prev",
                        MediaButtonReceiver
                                .buildMediaButtonPendingIntent(this,
                                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)))
                .apply {
                    if (mediaSession.controller.playbackState.state
                            == PlaybackStateCompat.STATE_PLAYING) {
                        addAction(NotificationCompat.Action(
                                R.drawable.exo_controls_pause, "pause",
                                MediaButtonReceiver
                                        .buildMediaButtonPendingIntent(this@MusicService,
                                                PlaybackStateCompat.ACTION_PAUSE)))
                    } else {
                        addAction(NotificationCompat.Action(
                                R.drawable.exo_controls_play, "play",
                                MediaButtonReceiver
                                        .buildMediaButtonPendingIntent(this@MusicService,
                                                PlaybackStateCompat.ACTION_PLAY)))
                    }
                }
                .applyAddAction(NotificationCompat.Action(
                        R.drawable.exo_controls_next, "next",
                        MediaButtonReceiver
                                .buildMediaButtonPendingIntent(this,
                                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT)))
                .build())

        if (mediaSession.controller.playbackState.state
                != PlaybackStateCompat.STATE_PLAYING) {
            stopForeground(false)
        }
    }
}
