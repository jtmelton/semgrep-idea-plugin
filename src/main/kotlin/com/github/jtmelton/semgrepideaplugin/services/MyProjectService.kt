package com.github.jtmelton.semgrepideaplugin.services

import com.github.jtmelton.semgrepideaplugin.MyBundle
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println("inside the MyProjectService")
        println(MyBundle.message("projectService", project.name))
        Logger.getInstance("MyProjectService").error("test MyProjectService")
    }
}
