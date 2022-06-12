package knf.hydra.module.jkanime

import knf.hydra.core.HeadConfig
import knf.hydra.core.HeadModule
import knf.hydra.core.HeadRepository
import knf.hydra.module.jkanime.extras.Repository
import knf.hydra.module.jkanime.extras.JkConfig

class Module: HeadModule() {
    override val moduleVersionCode: Int = BuildConfig.VERSION_CODE
    override val moduleVersionName: String = BuildConfig.VERSION_NAME
    override val baseUrl: String = "https://Jkanime.net"
    override val moduleName: String = "JkAnime"
    override val dataRepository: HeadRepository = Repository()
    override val config: HeadConfig = JkConfig()
}