package com.menak.login.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun copyImageToInternalStorage(context: Context, sourceUri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null

        val imagesDir = File(context.filesDir, "category_icons")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }

        val file = File(imagesDir, "${UUID.randomUUID()}.jpg")

        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}