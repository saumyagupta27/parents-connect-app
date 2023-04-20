package com.example.parentsconnect

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircularImageView (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas) {
        val drawable: Drawable? = drawable

        if (drawable == null) {
            return
        }

        if (width == 0 || height == 0) {
            return
        }

        val b: Bitmap = (drawable as BitmapDrawable).bitmap
        val bitmap: Bitmap = b.copy(Bitmap.Config.ARGB_8888, true)

        val w: Int = width
        val h: Int = height

        val roundBitmap: Bitmap = getRoundedCroppedBitmap(bitmap, w)
        canvas.drawBitmap(roundBitmap, 0f, 0f, null)
    }
    companion object {
        fun getRoundedCroppedBitmap(bitmap: Bitmap, radius: Int): Bitmap {
            val finalBitmap: Bitmap = if (bitmap.width != radius || bitmap.height != radius)
                Bitmap.createScaledBitmap(bitmap, radius, radius, false)
            else
                bitmap

            val output: Bitmap = Bitmap.createBitmap(finalBitmap.width, finalBitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val paint = Paint()
            val rect = Rect(0, 0, finalBitmap.width, finalBitmap.height)

            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.parseColor("#BAB399")
            canvas.drawCircle(
                finalBitmap.width / 2 + 0.7f,
                finalBitmap.height / 2 + 0.7f,
                finalBitmap.width / 2 + 0.1f, paint
            )
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(finalBitmap, rect, rect, paint)

            return output
        }
    }
}