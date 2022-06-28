/*
 * Created by @UnbarredStream on 20/06/22 17:38
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 20/06/22 17:38
 */

package knf.hydra.module.cuevana.models

import androidx.annotation.Keep
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Entity
import androidx.room.PrimaryKey
import knf.hydra.core.models.ContentData
import knf.hydra.core.models.InfoModel
import knf.hydra.core.models.data.Category
import knf.hydra.core.models.data.LayoutType
import knf.hydra.module.cuevana.repository.ChaptersSource
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector
import kotlin.random.Random

@Entity
class CuevanaMovieInfo : InfoModel() {
    @PrimaryKey
    @Selector("link[rel=canonical]", attr = "href", converter = CuevanaDirectoryModel.IdConverter::class)
    override var id: Int = Random.nextInt()

    @Selector("h1.Title")
    override var name: String = "???"

    @Selector("link[rel=canonical]", attr="href")
    override var link: String = "Vars"

    @Selector("link[rel=canonical]", converter = CategoryConverter::class)
    override var category: Category = Category.MOVIE

    @Selector("link[rel=canonical]", converter = LayoutConverter::class)
    override var layoutType: LayoutType = LayoutType.SINGLE

    @Selector("div.Image img", converter = ImageExtractor::class)
    override var coverImage: String? = null

    @Selector("div.Description")
    override var description: String? = null

    @Selector("html", converter = ChaptersConverter::class)
    override var contentData: ContentData? = null




    class CategoryConverter: ElementConverter<Category> {
        override fun convert(node: Element, selector: Selector): Category {
            return if (node.text().substringBeforeLast("/").substringAfterLast("/") == "serie")
                 Category.SERIES
            else
                Category.MOVIE
        }
    }

    class ImageExtractor: ElementConverter<String> {
        override fun convert(node: Element, selector: Selector): String {
            return if (node.attr("data-src").contains("image.tmdb.org"))
                "https://image.tmdb.org/t/p/original/" + node.attr("data-src").substringAfterLast("/")
            else
                node.attr("data-src")
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
    class ChaptersConverter : ElementConverter<ContentData?> {
        override fun convert(node: Element, selector: Selector): ContentData {
            val link = node.selectFirst("link[rel=canonical]").attr("href")
            val id = link.substringAfterLast("/")
            val isMovie: Boolean = !link.substringBeforeLast("/").contains("serie")
            return if(isMovie){
                ContentData.Single(
                    CuevanaContentItemModel(id, link, 1.0, null)
                )
            } else {
                val chapList = node.select("ul.all-episodes > li").map {
                    ChapterInfo(
                        it.selectFirst("a").attr("href"),
                        it.selectFirst("img").attr("data-src")
                    )
                }
                ContentData.Multiple(Pager(
                    config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        ChaptersSource(
                            ChapterConstructor(
                                id,
                                link,
                                null,
                                chapList
                            )
                        )
                    }
                ).flow)
            }
        }
    }

    data class ChapterConstructor(
        val seriesId: String,
        val seriesLink: String,
        val thumbLinkBase: String?,
        val chapterList: List<ChapterInfo>
    )

    data class ChapterInfo(
        val chapterLink: String,
        val thumbLink: String,
        val tempNumber: Int? = chapterLink.substringAfterLast("-").substringBeforeLast("x").toIntOrNull(),
        val chapNumber: Int? = chapterLink.substringAfterLast("-").substringAfterLast("x").toIntOrNull()
    )

}