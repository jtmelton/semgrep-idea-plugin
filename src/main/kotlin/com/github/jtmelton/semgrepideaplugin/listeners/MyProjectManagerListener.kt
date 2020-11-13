package com.github.jtmelton.semgrepideaplugin.listeners

import com.github.jtmelton.semgrepideaplugin.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        println("inside the MyProjectManagerListener")
        project.service<MyProjectService>()
    }
}
