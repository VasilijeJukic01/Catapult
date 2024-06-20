package com.example.catapult.ui.compose.avatar

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.catapult.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.random.Random

fun getRandomAvatar(): String {
    val avatars = listOf(
        "@Default1", "@Default2", "@Default3",
        "@Default4", "@Default5", "@Default6"
    )
    val randomIndex = Random.nextInt(avatars.size)

    return avatars[randomIndex]
}

fun getAvatarResource(avatar: String): Int {
    return when (avatar) {
        "@Default1" -> R.drawable.a1
        "@Default2" -> R.drawable.a2
        "@Default3" -> R.drawable.a3
        "@Default4" -> R.drawable.a4
        "@Default5" -> R.drawable.a5
        "@Default6" -> R.drawable.a6
        else -> R.drawable.a1
    }
}

fun copyImageToAppDir(context: Context, contentResolver: ContentResolver, uri: Uri, filename: String): Uri? {
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val uniqueFilename = "${System.currentTimeMillis()}_$filename"
    val file = File(context.filesDir, uniqueFilename)
    val outputStream = FileOutputStream(file)

    inputStream?.use { input ->
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024)
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }

    return Uri.fromFile(file)
}
