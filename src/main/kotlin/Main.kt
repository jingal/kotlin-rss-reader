import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun main() {
    val factory = DocumentBuilderFactory.newInstance()
    val xml = factory.newDocumentBuilder()
        .parse("https://techblog.woowahan.com/feed")
    val items = xml.getElementsByTagName("item")

    val num = items.length
    println(num)

    var list = mutableListOf<Pair<String, LocalDateTime>>()

    for ( i in 0 until items.length) {

        val item = items.item(i) as Element

        val title = item.getElementsByTagName("title").item(0).textContent
        println(title)

        val pubDate = item.getElementsByTagName("pubDate").item(0).textContent
        println(pubDate.substring(0..25).split(",")[1].trim())

        // DateTimeFormatter 패턴 정의
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH)

        // 문자열을 LocalDateTime으로 파싱
        val dateTime = LocalDateTime.parse(pubDate, formatter)
        list.add(Pair<String, LocalDateTime>(title, dateTime))
    }

    list.sortBy { it.second }

    println(list)
}
