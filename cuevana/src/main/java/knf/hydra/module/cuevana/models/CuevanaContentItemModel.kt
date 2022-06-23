/*
 * Created by @UnbarredStream on 20/06/22 21:52
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 20/06/22 21:52
 */

package knf.hydra.module.cuevana.models

import knf.hydra.core.models.ContentItemModel
import knf.hydra.core.models.data.LinkData
import kotlin.random.Random

class CuevanaContentItemModel(): ContentItemModel() {
    override var itemLink: LinkData = LinkData("https://embedsito.com/v/j-nlgady1rdgelj")
    override var id: Int = Random.nextInt()
    override var type: String = "Chapter %s"
    override var number: Double = 1.0
    override var itemTitle: String? = "asdasd"
    override var thumbnailLink: String? = ""
    override var commentsLink: String? = null
    override var isMedia: Boolean = true
}