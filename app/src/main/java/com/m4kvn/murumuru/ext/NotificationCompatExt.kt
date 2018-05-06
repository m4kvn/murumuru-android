package com.m4kvn.murumuru.ext

import android.app.PendingIntent
import android.support.annotation.ColorInt
import android.support.v4.app.NotificationCompat

fun NotificationCompat.Builder.applySetContentTitle(title: CharSequence?) =
        apply { setContentTitle(title) }

fun NotificationCompat.Builder.applySetDeleteIntent(pendingIntent: PendingIntent) =
        apply { setDeleteIntent(pendingIntent) }

fun NotificationCompat.Builder.applySetVisibility(visibility: Int) =
        apply { setVisibility(visibility) }

fun NotificationCompat.Builder.applySetSmallIcon(icon: Int) =
        apply { setSmallIcon(icon) }

fun NotificationCompat.Builder.applySetColor(@ColorInt argb: Int) =
        apply { color = argb }

fun NotificationCompat.Builder.applySetStyle(style: NotificationCompat.Style) =
        apply { setStyle(style) }

fun NotificationCompat.Builder.applyAddAction(action: NotificationCompat.Action) =
        apply { addAction(action) }

fun NotificationCompat.Builder.applySetContentIntent(intent: PendingIntent) =
        apply { setContentIntent(intent) }