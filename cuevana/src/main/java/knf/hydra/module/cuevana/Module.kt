/*
 * Created by @UnbarredStream on 12/06/22 18:40
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 18:30
 */

package knf.hydra.module.cuevana

import knf.hydra.core.HeadConfig
import knf.hydra.core.HeadModule
import knf.hydra.core.HeadRepository
import knf.hydra.module.cuevana.extras.Repository
import knf.hydra.module.cuevana.extras.CuevanaConfig

class Module: HeadModule() {
    override val moduleVersionCode: Int = BuildConfig.VERSION_CODE
    override val moduleVersionName: String = BuildConfig.VERSION_NAME
    override val baseUrl: String = "https://ww1.cuevana3.me/"
    override val moduleName: String = "Cuevana"
    override val dataRepository: HeadRepository = Repository()
    override val config: HeadConfig = CuevanaConfig()
}