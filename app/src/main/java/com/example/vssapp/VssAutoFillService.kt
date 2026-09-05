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

    fun autofillData(data: List<String>) {
        val rootNode = rootInActiveWindow ?: return
        val inputFields = mutableListOf<AccessibilityNodeInfo>()
        findEditTextNodes(rootNode, inputFields)

        for (i in 0 until minOf(data.size, inputFields.size)) {
            val args = Bundle().apply {
                putCharSequence(
                    AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    data[i]
                )
            }
            inputFields[i].performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
        }
    }

    private fun findEditTextNodes(node: AccessibilityNodeInfo?, list: MutableList<AccessibilityNodeInfo>) {
        if (node == null) return
        if (node.className == "android.widget.EditText") {
            list.add(node)
        }
        for (i in 0 until node.childCount) {
            findEditTextNodes(node.getChild(i), list)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        if (instance == this) instance = null
    }
}