package com.example.smarttaskmanager.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smarttaskmanager.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var headerTitle: TextView
    private lateinit var backButton: ImageView
    private var customBackAction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        headerTitle = findViewById(R.id.header_title)
        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            customBackAction?.invoke() ?: finish()
        }
    }

    fun setCustomHeader(
        title: String,
        tittleVisible: Boolean? = false,
        backButtonVisible: Boolean? = false,
    ) {
        if (tittleVisible == true) {
            headerTitle.visibility = View.VISIBLE
            headerTitle.text = title
        }
        if (backButtonVisible == true) {
            backButton.visibility = View.VISIBLE
            customBackAction = { onBackPressed() }
        }else{
            backButton.visibility = View.GONE
        }
    }
}
