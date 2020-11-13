package com.github.jtmelton.semgrepideaplugin.services

import com.github.jtmelton.semgrepideaplugin.MyBundle

class MyApplicationService {

    init {
        println("inside the MyApplicationService")
        println(MyBundle.message("applicationService"))
    }
}
