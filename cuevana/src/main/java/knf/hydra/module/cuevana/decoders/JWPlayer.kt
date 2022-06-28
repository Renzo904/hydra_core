/*
 * Created by @UnbarredStream on 26/06/22 18:04
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 26/06/22 18:04
 */

package knf.hydra.module.cuevana.decoders

import knf.hydra.core.models.data.DecodeResult
import knf.hydra.core.models.data.Option
import knf.hydra.core.models.data.SourceItem
import knf.hydra.core.models.data.VideoDecoder
import org.json.JSONObject
import java.net.URL

class JWPlayer: VideoDecoder() {
    override fun canDecode(link: String): Boolean = link.contains("tomatomatela")

    override suspend fun decode(item: SourceItem): DecodeResult {
        return try {
            val json = JSONObject(URL("https://tomatomatela.com/details.php?v=${item.link.substringAfterLast("#")}").readText())
            DecodeResult.Success(listOf(Option(json.getString("file").replace("\\", ""))))
        }catch (e:Exception){
            e.printStackTrace()
            DecodeResult.Failed()
        }
    }
}