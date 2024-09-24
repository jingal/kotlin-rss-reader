package model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
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

    fun getItemList(items : NodeList) : RssItems {
        val rssItems = RssItems(mutableListOf<RssItem>())

        for (i in 0 until items.length) {
            val item = items.item(i) as Element

            val title = item.getElementsByTagName("title").item(0).textContent
            val description = item.getElementsByTagName("description").item(0).textContent
            val pubDate = item.getElementsByTagName("pubDate").item(0).textContent

            rssItems.add(RssItem(title, description, pubDate))
        }

        return rssItems
    }
}