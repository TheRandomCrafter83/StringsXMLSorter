package com.coderzf1.stringsxmlsorter


import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text
import org.simpleframework.xml.core.Persister
import java.awt.BorderLayout
import java.io.OutputStreamWriter
import java.io.StringWriter
import javax.swing.JComponent
import javax.swing.JPanel

class MyDemoAction:AnAction() {

    override fun update(e: AnActionEvent) {
        val currentFile:VirtualFile? = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        val filename = currentFile?.name
        if (filename != "strings.xml"){
            e.presentation.isEnabled = false
        }
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        FileDocumentManager.getInstance().saveAllDocuments()
        val sortDialog = SortDialogWrapper()

        if (sortDialog.showAndGet()){
            when(sortDialog.sortBy){
                "Value"->{
                    sortByValue(e)
                }
                "Name"->{
                    sortByName(e)
                }
            }
        }
    }

    private fun sortByValue(e: AnActionEvent){
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val document = editor.document
        val serializer = Persister()
        val resources = serializer.read(Resources::class.java, document.text)
        var sortedResources = resources.xmlStringList?.sortedWith(
            compareBy {
                it.value?.lowercase()
            }
        )
        val writer = StringWriter()
        resources.xmlStringList = sortedResources
        serializer.write(resources,writer)
        WriteCommandAction.runWriteCommandAction(editor.project) {
            document.setText(writer.toString())
        }.run {

        }
        FileDocumentManager.getInstance().saveAllDocuments()
    }

    private fun sortByName(e: AnActionEvent){
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val document = editor.document
        val serializer = Persister()
        val resources = serializer.read(Resources::class.java, document.text)
        var sortedResources = resources.xmlStringList?.sortedWith(
            compareBy {
                it.name?.lowercase()
            }
        )
        val writer = StringWriter()
        resources.xmlStringList = sortedResources
        serializer.write(resources,writer)
        WriteCommandAction.runWriteCommandAction(editor.project) {
            document.setText(writer.toString())
        }.run {

        }
        FileDocumentManager.getInstance().saveAllDocuments()
    }
}

class SortDialogWrapper:DialogWrapper(true){

    var sortBy: String = "Name"

    init {
        title = "Sort By"
        init()
    }
    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout())
        val combo = ComboBox<String>()
        combo.addItem("Name")
        combo.addItem("Value")
        combo.addActionListener {
            if(it.actionCommand == "comboBoxChanged"){
                sortBy = combo.selectedItem?.toString()?:"Value"
            }
        }
        dialogPanel.add(combo, BorderLayout.CENTER)
        return dialogPanel
    }

}

@Root(name="string", strict = false)
class XmlString{
    @set:Text(required = false)
    @get:Text(required = false)
    var value:String? = null

    @set:Attribute(required = false, name = "name")
    @get:Attribute(required = false, name = "name")
    var name:String? = null

    override fun toString(): String {
        return "$name : $value"
    }
}

@Root(name = "resources", strict = false)
class Resources {
    @get:ElementList(entry = "string", name = "resources", required = false, inline = true)
    @set:ElementList(entry = "string", name = "resources", required = false, inline = true)
    var xmlStringList: List<XmlString>? = null
}

//<resources>
//<string name="app_name">Screen Size Support</string>
//</resources>