package model

import view.RssView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RssData ( val rssItems : RssItems, val rssView : RssView ){
    fun updateData(newRssItems : RssItems) {
        var prevSize = rssItems.size

        for( item in newRssItems) {
            if (!rssItems.contains(item)) {
                rssItems.add(item)
            }
        }

        if (prevSize < rssItems.size) {
            rssView.printUpdated()
            rssItems.sortByDescending { LocalDateTime.parse(it.pubDate, DateTimeFormatter.RFC_1123_DATE_TIME) }
        }
    }

    fun search(search : String) {
        rssItems.filter { it.title.contains(search) }.forEach {
            rssView.printSearchResult(Triple(it.pubDate, it.title, it.description))
        }
    }
}