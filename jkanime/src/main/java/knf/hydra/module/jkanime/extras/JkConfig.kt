/*
 * Created by @UnbarredStream on 08/04/22 19:35
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 08/04/22 19:32
 */

package knf.hydra.module.jkanime.extras

import knf.hydra.core.HeadConfig
import knf.hydra.core.models.data.ReviewConfig

class JkConfig: HeadConfig() {
    init {
        isRecentsAvailable = true
        isCalendarEnabled = true
        isDirectoryAvailable = true
        isSearchAvailable = true
        reviewConfig = ReviewConfig(
            starsState = ReviewConfig.State.DISABLED,
            commentaryState = ReviewConfig.State.DISABLED,
            singleReview = true,
            minRating = 1.0
        )
    }
}