package com.gpetuhov.android.samplekotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@FlowPreview    // currently Kotlin Flows are experimental, so we need this annotation
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_button.setOnClickListener { startCount() }
        reset_button.setOnClickListener { resetCount() }
    }

    // === Private methods ===

    private fun startCount() {
        // TODO: change this to activity scope
        GlobalScope.launch(Dispatchers.Main) {
            val intFlow = flow {
                (0..10).forEach {
                    delay(100)
                    emit(it)
                }
            }

            intFlow.collect {
                counter.text = it.toString()
            }
        }
    }

    private fun resetCount() {
        counter.text = "0"
    }
}
