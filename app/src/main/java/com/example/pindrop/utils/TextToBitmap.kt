package com.example.pindrop.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint


public class TextToBitmap {
    //method to convert your text to image
    fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap? {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val baseline: Float = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.1f).toInt() // round
        val height = (baseline + paint.descent() + 0.1f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, 0.1f, baseline, paint)
        return image
    }
}