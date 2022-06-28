/*
 * Created by @UnbarredStream on 12/06/22 19:11
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 18:40
 */

package knf.hydra.module.cuevana.extras

import knf.hydra.core.HeadConfig
import knf.hydra.core.models.data.BypassBehavior
import knf.hydra.core.models.data.DialogStyle
import knf.hydra.module.cuevana.decoders.GoCDN
import knf.hydra.module.cuevana.decoders.JWPlayer

class CuevanaConfig: HeadConfig() {
    init {
        isRecentsAvailable = true
        isSearchAvailable = true
        isDirectoryAvailable = true
        isSearchSuggestionsAvailable = true
        isCastEnabled = true
        bypassBehavior = BypassBehavior.Custom(
            useLastUA = false,
            skipCaptcha = true,
            useDialog = true,
            dialogStyle = DialogStyle.CLASSIC
        )
        customDecoders = listOf(
            GoCDN(),
            JWPlayer()
        )
        searchBarText = "Buscar Peliculas o Series"
    }
}