/*
 * Created by @UnbarredStream on 12/06/22 18:41
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 18:41
 */

package knf.hydra.module.cuevana.models

import android.util.Log
import androidx.room.Entity
import knf.hydra.core.models.DirectoryModel
import knf.hydra.core.models.data.Category
import knf.hydra.core.models.data.LayoutType
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector
import kotlin.random.Random


@Entity(tableName = "calendar")
class CuevanaDirectoryModel: DirectoryModel() {
    @Selector("div > a", attr="href", converter = IdConverter::class)
    override var id: Int = 0

    @Selector("h2.Title")
    override var name: String = ""

    @Selector("div > a", attr = "href")
    override var infoLink: String = ""

    @Selector("img", attr = "data-src")
    override var imageLink: String? = ""

    override var type: String? = null

    override var category: Category = Category.MOVIE


    override var infoLayoutType: LayoutType = LayoutType.UNKNOWN

    class IdConverter: ElementConverter<Int> {
        override fun convert(node: Element, selector: Selector): Int {
            val id = node.text().substringAfterLast("/")
            return id.hashCode()
        }
    }


}



class DirectoryPage{
    @Selector("ul.MovieList.Rows li")
    var list = emptyList<CuevanaDirectoryModel>()
}