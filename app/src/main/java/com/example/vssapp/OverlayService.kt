package com.example.vssapp

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import java.util.Locale

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: LinearLayout

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        floatingView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#CC000000"))
            setPadding(16, 16, 16, 16)
        }

        val btnFill = Button(this).apply {
            text = "VSS: Wzmocnione\n[Uzupełnij]"
            textSize = 12f
            setBackgroundColor(Color.parseColor("#0066FF"))
            setTextColor(Color.WHITE)
        }

        btnFill.setOnClickListener {
            // 1. Wywołanie generatora (zwraca VssResult z wylosowaną deltą i wynikami)
            val generatedResult = VssDataGenerator.generateRandomizedWzmocnionePodloze()

            // 2. Wyciągnięcie listy liczb i zamiana na napisy (np. "0.19")
            val formattedData: List<String> = generatedResult.settlements.map { value ->
                String.format(Locale.US, "%.2f", value)
            }

            val autoFill = VssAutoFillService.instance

            if (autoFill != null) {
                // 3. Przekazanie poprawnego typu List<String> do autowypełniania
                autoFill.autofillData(formattedData)
                val deltaFormatted = String.format(Locale.US, "%.2f", generatedResult.deltaUsed)
                Toast.makeText(this, "Uzupełniono 23 pola! (Delta: +$deltaFormatted mm)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Włącz najpierw Usługę Dostępności!", Toast.LENGTH_LONG).show()
            }
        }

        floatingView.addView(btnFill)

        val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 300
        }

        floatingView.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager.updateViewLayout(floatingView, params)
                        return true
                    }
                }
                return false
            }
        })

        windowManager.addView(floatingView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
    }
}