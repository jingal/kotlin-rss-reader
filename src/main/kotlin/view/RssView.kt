package view

class RssView {
    fun printUpdated() {
        println("RSS 데이터가 업데이트되었습니다.")
    }

    fun printSearchResult( results : Triple<String, String, String>) {
        println(results.first)
        println(results.second)
        println(results.third)
        println()
    }
}