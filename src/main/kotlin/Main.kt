import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import org.w3c.dom.NodeList


fun main() {
    val factory = DocumentBuilderFactory.newInstance()
    val xml = factory.newDocumentBuilder()
        .parse("https://techblog.woowahan.com/feed")
    val items = xml.getElementsByTagName("item")

    val num = items.length
    println(num)

    for ( i in 0 until items.length) {
        val item = items.item(i) as Element

        val title = item.getElementsByTagName("title").item(0).textContent
        println(title)

        val pubDate = item.getElementsByTagName("pubDate").item(0).textContent
        println(pubDate)
    }
}
