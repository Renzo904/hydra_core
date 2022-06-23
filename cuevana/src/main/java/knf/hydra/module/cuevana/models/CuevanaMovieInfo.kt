/*
 * Created by @UnbarredStream on 20/06/22 17:38
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 20/06/22 17:38
 */

package knf.hydra.module.cuevana.models

import android.util.Log
import androidx.annotation.Keep
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import knf.hydra.core.models.ContentData
import knf.hydra.core.models.InfoModel
import knf.hydra.core.models.data.Category
import knf.hydra.core.models.data.LayoutType
import kotlinx.coroutines.delay
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector
import java.net.URL
import java.text.DecimalFormat
import kotlin.random.Random

@Entity
class CuevanaMovieInfo : InfoModel() {
    @PrimaryKey
    @Selector("link[rel=canonical]", attr = "href", converter = CuevanaDirectoryModel.IdConverter::class)
    override var id: Int = Random.nextInt()

    @Selector("h1.Title")
    override var name: String = "???"

    @Selector("link[rel=canonical]", attr="href")
    override var link: String = ""

    @Selector("link[rel=canonical]", converter = CategoryConverter::class)
    override var category: Category = Category.MOVIE

    @Selector("link[rel=canonical]", converter = LayoutConverter::class)
    override var layoutType: LayoutType = LayoutType.SINGLE

    @Selector("div.Image img", attr = "abs:data-src")
    override var coverImage: String? = null

    @Selector("div.Description")
    override var description: String? = null

    @Ignore
    @Selector("link[rel=canonical]", converter = ChaptersConverter::class)
    override var contentData: ContentData? = null




    class CategoryConverter: ElementConverter<Category> {
        override fun convert(node: Element, selector: Selector): Category {
            return if (node.text().substringBeforeLast("/").substringAfterLast("/") == "serie")
                 Category.SERIES
            else
                Category.MOVIE
        }
    }

    class LayoutConverter: ElementConverter<LayoutType> {
        override fun convert(node: Element, selector: Selector): LayoutType {
            return if (node.text().substringBeforeLast("/").substringAfterLast("/") == "serie")
                LayoutType.MULTIPLE
            else
                LayoutType.SINGLE
        }
    }

    @Keep
    class ChaptersConverter @Keep constructor() : ElementConverter<ContentData?> {
        override fun convert(node: Element, selector: Selector): ContentData? {
            Log.d("SOURCEDATADEBUG", "Esta en chaptersConverter")
            return ContentData.Single(
                CuevanaContentItemModel()
            )
        }
    }
}