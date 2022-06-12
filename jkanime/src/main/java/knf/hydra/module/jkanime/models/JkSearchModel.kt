/*
 * Created by @UnbarredStream on 11/06/22 22:00
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/06/22 22:00
 */

package knf.hydra.module.jkanime.models

import androidx.room.Entity
import knf.hydra.core.models.DirectoryModel
import knf.hydra.core.models.data.Category
import knf.hydra.core.models.data.LayoutType
import knf.hydra.core.models.data.RankingData
import pl.droidsonroids.jspoon.annotation.Selector



class JkSearchModel: DirectoryModel() {
    @Selector("div.col-md-5.custom_thumb2 > a", attr="href", converter = JkDirectoryModel.IdConverter::class)
    override var id: Int = 0
    
    override var name: String = ""

    override var infoLink: String = ""

    override var imageLink: String? = ""

    override var type: String? = ""
    override var category: Category = Category.ANIME

    override var infoLayoutType: LayoutType = LayoutType.UNKNOWN

    override var rankingData: RankingData? = null

}

class SearchPage{
    @Selector("div.anime__page__content > div.row > div")
    var list = emptyList<JkSearchModel>()
}