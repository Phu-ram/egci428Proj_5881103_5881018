package com.example.project_personaldoctor.Helper

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
// HTTP helper can be used to connect to JSON API as like in OK HTTPS
//work like response return as a string, which use as a user object

class HTTPHelper {
    //    public Helper(){
    //
    //    }
    fun getHTTPData(urlString: String): String {
        try {
            val url = URL(urlString)
            val urlConnection = url.openConnection() as HttpURLConnection
            if (urlConnection.responseCode == 200) {
                val r = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val sb = StringBuilder()
                var line = r.readLine()

                while (line != null) {
                    sb.append(line)
                    line = r.readLine()
                }
                stream = sb.toString()
                urlConnection.disconnect()
            }

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stream!!
    }

    companion object {
        internal var stream: String? = null
    }

}