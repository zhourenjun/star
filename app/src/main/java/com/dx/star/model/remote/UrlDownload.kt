package com.dx.star.model.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 *
 * java类作用描述
 * zrj 2021/8/12 17:43
 * 更新者 2021/8/12 17:43
 */
class UrlDownload {
    companion object {
        suspend fun asyncDownload(url: String, fileName: String, path: String): Boolean {
            return withContext(Dispatchers.IO) { download(url, fileName, path) }
        }

        fun download(url: String, fileName: String, path: String): Boolean {
            var urlConn: HttpURLConnection? = null
            val outputFile = createFile(path, fileName)
            try {
                urlConn = URL(url).openConnection() as HttpURLConnection
                urlConn.inputStream.use { input ->
                    BufferedOutputStream(FileOutputStream(outputFile)).use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConn?.disconnect()
            }
            return true
        }

        /**
         * 递归创建文件夹
         */
        @Throws(IOException::class)
        fun createFile(path: String, fileName: String): File? {
            val filePath = createDir(path)
            if (filePath != null) {
                val file = File(filePath, fileName)
                if (file.exists()) {
                    file.delete()
                }
                file.createNewFile()
                return file
            }
            return null
        }

        /**
         * 递归创建文件夹
         */
        fun createDir(path: String): File? {
            val file = File(path)
            if (file.exists()) return file
            val p = file.parentFile
            if (!p.exists()) {
                createDir(p.path)
            }
            return if (file.mkdir()) file else null
        }
    }
}