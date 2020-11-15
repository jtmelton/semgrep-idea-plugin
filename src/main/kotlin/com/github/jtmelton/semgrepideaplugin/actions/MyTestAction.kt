package com.github.jtmelton.semgrepideaplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager

class MyTestAction : AnAction() {
    override fun update(e: AnActionEvent) {
        // Using the event, evaluate the context, and enable or disable the action.
        println("inside the MyTestAction action 1")
//        Logger.getInstance("MyTestAction").error("action test MyTestAction 1")
    }

    override fun actionPerformed(e: AnActionEvent) {
        val document = e.getRequiredData(CommonDataKeys.EDITOR).document
        println("Doc text is $document.text")

        val fileName = FileDocumentManager.getInstance().getFile(document)?.path
        println("filename is $fileName")

        // Using the event, implement an action. For example, create and show a dialog.
        println("inside the MyTestAction action 2")
//        Logger.getInstance("MyTestAction").error("action test MyTestAction 2")
    }
}
