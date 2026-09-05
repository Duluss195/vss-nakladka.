package com.example.vssapp

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class VssAutoFillService : AccessibilityService() {

    companion object {
        var instance: VssAutoFillService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    fun autofillData(values: List<String>) {
        val rootNode = rootInActiveWindow ?: return
        val inputFields = mutableListOf<AccessibilityNodeInfo>()
        findEditableNodes(rootNode, inputFields)

        for (i in 0 until minOf(inputFields.size, values.size)) {
            val field = inputFields[i]
            val arguments = Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, values[i])
            }
            field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
        }
    }

    private fun findEditableNodes(node: AccessibilityNodeInfo, list: MutableList<AccessibilityNodeInfo>) {
        if (node.isEditable) {
            list.add(node)
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            findEditableNodes(child, list)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}