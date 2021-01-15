package ru.startandroid.develop.camera

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.picker_view.view.*

class PickerView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var listener: OnPickerListener? = null

    init {
        View.inflate(context, R.layout.picker_view, this)
        pickerIV.setOnClickListener { listener?.onPickImage() }
        clearIV.setOnClickListener { clearImage() }

        // Read data from attrs
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.PickerView,
                0, 0
        )
        try {
            val resId = a.getResourceId(R.styleable.PickerView_imageResource, -1)
            setImageResource(resId)
        } catch (error: Throwable) {
        } finally {
            a.recycle()
        }
    }

    fun setListener(listener: OnPickerListener) {
        this.listener = listener
    }

    fun setImageUri(uri: Uri) {
        pickerIV.setImageURI(uri)
    }

    private fun clearImage() {
        pickerIV.setImageResource(R.drawable.camera)
    }

    private fun setImageResource(resId : Int)
    {
        pickerIV.setImageResource(resId)
    }

    interface OnPickerListener {
        fun onPickImage()
    }
}