/*
 * Created by @UnbarredStream on 12/06/22 18:41
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 18:41
 */

package knf.hydra.module.cuevana.models

import knf.hydra.core.models.DirectoryModel
import knf.hydra.core.models.data.Category
import knf.hydra.core.models.data.LayoutType
import knf.hydra.core.models.data.RankingData
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector
import kotlin.random.Random


class CuevanaSearchModel: DirectoryModel() {
    @Selector("div > a", attr = "href", converter = CuevanaDirectoryModel.IdConverter::class)
    override var id: Int = Random.nextInt()

    @Selector("h2.Title")
    override var name: String = ""

    @Selector("div > a", attr = "href")
    override var infoLink: String = ""

    @Selector("img", attr = "data-src")
    override var imageLink: String? = ""

    @Selector("span.Qlty")
    override var type: String? = ""

    @Selector("span.Qlty", converter = CategoryConverter::class)
    override var category: Category = Category.UNKNOWN

    @Selector("span.Qlty", converter = LayoutConverter::class)
    override var infoLayoutType: LayoutType = LayoutType.UNKNOWN

    override var rankingData: RankingData? = null


    class CategoryConverter: ElementConverter<Category> {
        override fun convert(node: Element, selector: Selector): Category {
            return if (node.text() == "SERIE")
                Category.SERIES
            else
                Category.MOVIE
        }
    }

    class LayoutConverter: ElementConverter<LayoutType> {
        override fun convert(node: Element, selector: Selector): LayoutType {
            return if (node.text() == "SERIE")
                LayoutType.MULTIPLE
            else
                LayoutType.SINGLE
        }
    }

}

class SearchPage{
    @Selector("ul.MovieList.Rows li")
    var list = emptyList<CuevanaSearchModel>()
}