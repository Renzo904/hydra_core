/*
 * Created by @UnbarredStream on 20/06/22 21:52
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 20/06/22 21:52
 */

package knf.hydra.module.cuevana.models

import knf.hydra.core.models.ContentItemModel
import knf.hydra.core.models.data.LinkData
import kotlin.random.Random

class CuevanaContentItemModel(seriesId: String, link: String, chapterNumber: Double, thumbLink: String?): ContentItemModel() {
    override var itemLink: LinkData = LinkData(link)
    override var id: Int = "$seriesId-$chapterNumber".hashCode()
    override var type: String = "Chapter %s"
    override var number: Double = chapterNumber
    override var itemTitle: String? = null
    override var thumbnailLink: String? = thumbLink
    override var commentsLink: String? = null
    override var isMedia: Boolean = true
}