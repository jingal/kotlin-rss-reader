package model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RssService(
    private val scope : CoroutineScope,
    private val dispatcher: CoroutineDispatcher,
    val urlList : List<String>,
    val rssData : RssData
) {
    val rssXmlUtil = RssXmlUtil(dispatcher)
//    val delayTime = if (isTestMode) 1000L else 10 * 60 * 1000L

    suspend fun asyncGetRssItemFromLink(link: String): RssItems = withContext(dispatcher){
        val items = rssXmlUtil.rssItemParser(link)
        rssXmlUtil.getItemList(items)
    }

    fun rssDispatching() {
        scope.launch {
            while (isActive) {
                val rssItems = RssItems(mutableListOf<RssItem>())
                val deferredList = mutableListOf<Deferred<RssItems>>()

                for(s in urlList) {
                    val deferred = async {
                        asyncGetRssItemFromLink(s)
                    }
                    deferredList.add(deferred)
                }

                val result = deferredList.awaitAll()

                result.forEach { rssItem ->
                    rssItems.items.addAll(rssItem.items)  // rssItems 내부 리스트에 추가
                }

                rssData.updateData(rssItems)
                delay( 1 * 1000L) // 10분 (밀리초 단위)
            }
        }
    }


}