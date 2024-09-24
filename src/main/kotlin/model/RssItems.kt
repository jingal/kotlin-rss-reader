package model

data class RssItems(val items : MutableList<RssItem>) :
    MutableList<RssItem> by items {
}
