package util

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.books.Books

object GAPIUtil {

    private val net: NetHttpTransport = new NetHttpTransport()

    private val jack = JacksonFactory.getDefaultInstance

    private val cred: GoogleCredential = new GoogleCredential()

    val books: Books = new Books.Builder(net, jack, cred).setApplicationName(buildinfo.BuildInfo.name).build()

}
