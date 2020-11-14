package com.github.jtmelton.semgrepideaplugin.services

import com.github.jtmelton.semgrepideaplugin.MyBundle
import com.intellij.openapi.diagnostic.Logger

class MyApplicationService {

    init {
        println("inside the MyApplicationService")
        println(MyBundle.message("applicationService"))
        Logger.getInstance("MyApplicationService").error("test MyApplicationService")
    }
}
