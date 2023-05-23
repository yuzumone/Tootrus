package net.yuzumone.tootrus.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.media.PostMediaUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostStatusParams
import net.yuzumone.tootrus.domain.mastodon.status.PostStatusUseCase
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.math.floor

@AndroidEntryPoint
class PostStatusService : Service() {

    @Inject
    lateinit var postStatusUseCase: PostStatusUseCase
    @Inject
    lateinit var postMediaUseCase: PostMediaUseCase
    private lateinit var lifecycleScope: LifecycleCoroutineScope

    companion object {
        private const val ARG_POST_STATUS_PARAMS = "post_status_params"
        fun createIntent(context: Context, params: Params): Intent {
            return Intent(context, PostStatusService::class.java).apply {
                putExtra(ARG_POST_STATUS_PARAMS, Gson().toJson(params))
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        lifecycleScope = ProcessLifecycleOwner.get().lifecycleScope
        val files = ArrayList<MultipartBody.Part>()
        intent?.getStringExtra(ARG_POST_STATUS_PARAMS)?.let {
            val params = Gson().fromJson(it, Params::class.java)
            params.uris?.forEach { uri ->
                prepareUploadFile(Uri.parse(uri))?.let { file ->
                    files.add(
                        MultipartBody.Part.createFormData(
                            "file", file.name,
                            RequestBody.create(MediaType.parse("image/png"), file)
                        )
                    )
                }
            }
            if (files.size == 0) {
                val status = PostStatusParams(
                    params.status, params.inReplyToId, null,
                    params.sensitive, params.spoilerText, params.visibility
                )
                postStatus(status)
            } else {
                ProcessLifecycleOwner.get().lifecycle
                postMediaUseCase(files, lifecycleScope) { attachments ->
                    when (attachments) {
                        is Success -> {
                            val mediaIds = attachments.value.map { attachment -> attachment.id }
                            val status = PostStatusParams(
                                params.status, params.inReplyToId, mediaIds,
                                params.sensitive, params.spoilerText, params.visibility
                            )
                            postStatus(status)
                            stopSelf()
                        }
                        is Failure -> {
                            stopSelf()
                            showToast(R.string.error)
                        }
                    }
                }
            }
        }
        startForeground(1, createNotification())
        return START_NOT_STICKY
    }

    private fun postStatus(params: PostStatusParams) {
        postStatusUseCase(params, lifecycleScope) { result ->
            when (result) {
                is Success -> {
                    showToast(R.string.toot)
                    stopSelf()
                }
                is Failure -> {
                    showToast(R.string.error)
                    stopSelf()
                }
            }
        }
    }

    private fun createNotification(): Notification {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = getString(R.string.channel_id)
        val name = getString(R.string.channel_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            channel.enableLights(false)
            channel.enableVibration(false)
            channel.setSound(null, null)
            manager.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, id)
            .setSmallIcon(android.R.drawable.ic_menu_send)
            .setContentText(getString(R.string.toot_now))
            .setAutoCancel(true)
            .setSound(null)
            .setVibrate(null)
        return notificationBuilder.build()
    }

    private fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    private fun prepareUploadFile(uri: Uri): File? {
        val requestSize = 960
        val input = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".tmp", externalCacheDir)
        val fos = FileOutputStream(tempFile)
        val options = BitmapFactory.Options().also {
            it.inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(input, null, options)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            val sample = if (options.outWidth > options.outHeight) {
                options.outWidth / requestSize
            } else {
                options.outHeight / requestSize
            }
            if (sample > 2) {
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.setTargetSampleSize(sample)
                }.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            } else {
                ImageDecoder.decodeBitmap(source)
                    .compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
            fos.flush()
        } else {
            val imageScaleWidth = options.outWidth / requestSize
            val imageScaleHeight = options.outHeight / requestSize
            if (imageScaleWidth > 2 && imageScaleHeight > 2) {
                val imageScale = floor(requestSize.toDouble())
                val scaleOption = BitmapFactory.Options().also {
                    var i = 2
                    while (i <= imageScale) {
                        it.inSampleSize = i
                        i *= 2
                    }
                }
                BitmapFactory.decodeStream(
                    BufferedInputStream(input), null,
                    scaleOption
                )?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            } else {
                BitmapFactory.decodeStream(BufferedInputStream(input))
                    .compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
            fos.flush()
        }
        fos.close()
        input?.close()
        return tempFile
    }

    data class Params(
        val status: String,
        val inReplyToId: Long?,
        val uris: List<String>?,
        val sensitive: Boolean,
        val spoilerText: String?,
        val visibility: Status.Visibility
    )
}
