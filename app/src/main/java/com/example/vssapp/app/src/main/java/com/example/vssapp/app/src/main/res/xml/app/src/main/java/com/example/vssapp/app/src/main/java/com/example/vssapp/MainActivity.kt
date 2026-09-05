package com.example.vssapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStartOverlay = findViewById<Button>(R.id.btnStartOverlay)
        val btnAccessibility = findViewById<Button>(R.id.btnAccessibility)

        btnStartOverlay.setOnClickListener {
            checkOverlayPermissionAndStart()
        }

        btnAccessibility.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
            Toast.makeText(this, "Znajdź 'VSS Nakładka' i włącz ją", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkOverlayPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1234)
            Toast.makeText(this, "Zezwól na wyświetlanie nad innymi aplikacjami", Toast.LENGTH_LONG).show()
        } else {
            startService(Intent(this, OverlayService::class.java))
            finish() // Zamyka okno główne, zostawiając sam dymek
        }
    }
}