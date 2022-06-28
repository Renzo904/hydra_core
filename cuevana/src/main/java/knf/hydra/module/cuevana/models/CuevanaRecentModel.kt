/*
 * Created by @UnbarredStream on 12/06/22 18:41
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 18:41
 */

package knf.hydra.module.cuevana.models

import android.util.Log
import androidx.room.PrimaryKey
import knf.hydra.core.models.RecentModel
import knf.hydra.core.models.data.Category
import knf.hydra.module.cuevana.Module
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector
import java.lang.Math.log10
import kotlin.math.truncate
import kotlin.random.Random


class CuevanaRecentModel: RecentModel() {
    @PrimaryKey
    @Selector("article > a", attr = "href", converter = CuevanaDirectoryModel.IdConverter::class)
    override var id: Int = Random.nextInt()

    @Selector("article > a", attr = "href", converter = IdConverter::class)
    override var infoId: Int = Random.nextInt()

    @Selector("img", attr = "abs:data-src")
    override var infoImage: String? = null

    @Selector("a", attr = "abs:href")
    override var link: String = ""

    @Selector("a", converter = SeriesLinkExtractor::class)
    override var infoLink: String = ""

    @Selector("h2.Title", converter = TitleExtractor::class)
    override var name: String = ""

    @Selector("span.Year", converter = ChapterExtractor::class)
    override var number: Double = 0.0

    override var category: Category = Category.SERIES

    @Selector("span.Year", converter = ChapterNumberConverter::class)
    override var type: String = "Chapter %s"

    override var isMedia: Boolean = true

    class IdConverter: ElementConverter<Int> {
        override fun convert(node: Element, selector: Selector): Int {
            val infoLink = SeriesLinkExtractor().convert(node, selector)
            return CuevanaDirectoryModel.IdConverter().convert(Element(infoLink), selector).hashCode()
        }
    }

    class SeriesLinkExtractor: ElementConverter<String> {
        override fun convert(node: Element, selector: Selector): String {
            val id = node.attr("href").substringAfterLast("/").substringBeforeLast("-")
            return Module().baseUrl + "/serie/$id"
        }
    }

    class TitleExtractor: ElementConverter<String> {
        override fun convert(node: Element, selector: Selector): String {
            return node.text().substringBeforeLast(" ")
        }
    }

    class ChapterExtractor: ElementConverter<Double> {
        override fun convert(node: Element, selector: Selector): Double {
            val string = node.text().replace("x", ".")
            val number: Double? = string.toDoubleOrNull()
            return number ?: 0.0
        }
    }

    class ChapterNumberConverter: ElementConverter<String> {
        override fun convert(node: Element, selector: Selector): String {
            return "Chapter " + node.text()
        }
    }

}

class RecentsPage {
    @Selector("ul.MovieList.Rows.episodes li")
    var list = emptyList<CuevanaRecentModel>()
}