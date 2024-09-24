package model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

class RssXmlUtil(
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun rssItemParser(link: String): NodeList {
        return withContext(dispatcher) {
            val factory = DocumentBuilderFactory.newInstance()
            val xml = factory.newDocumentBuilder().parse(link)
            xml.getElementsByTagName("item")
        }
    }
}