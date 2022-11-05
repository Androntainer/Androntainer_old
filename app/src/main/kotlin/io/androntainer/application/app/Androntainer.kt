package io.androntainer.application.app

import io.androntainer.application.abstract.AndrontainerApplication

class Androntainer : AndrontainerApplication() {

    override fun initSdk() {
        initDynamicColors(this@Androntainer)
        initDialogX(this@Androntainer)
        initTaskbar(this@Androntainer)
    }
}