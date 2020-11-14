package com.github.jtmelton.semgrepideaplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger

class MyTestAction : AnAction() {
    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
        println("inside the MyTestAction action 1")
        Logger.getInstance("MyTestAction").error("action test MyTestAction 1")
    }

    override fun actionPerformed(e: AnActionEvent) {
        // Using the event, implement an action. For example, create and show a dialog.
        println("inside the MyTestAction action 2")
        Logger.getInstance("MyTestAction").error("action test MyTestAction 2")
    }
}
