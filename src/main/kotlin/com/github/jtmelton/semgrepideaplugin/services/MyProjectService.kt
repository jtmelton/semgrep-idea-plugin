package com.github.jtmelton.semgrepideaplugin.services

import com.intellij.openapi.project.Project
import com.github.jtmelton.semgrepideaplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
