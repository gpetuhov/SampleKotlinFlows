package com.gpetuhov.android.samplekotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber

@FlowPreview    // currently Kotlin Flows are experimental, so we need this annotation
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_button.setOnClickListener { startCount() }
        reset_button.setOnClickListener { resetCount() }
    }

    // === Private methods ===

    private fun startCount() {
        // TODO: refactor this out of here
        // GlobalScope is used for simplicity, but activity scope should be used instead.
        GlobalScope.launch(Dispatchers.Main) {
            val intFlow = flow {
                Timber.tag(TAG).d("Start flow")

                (0..10).forEach {
                    delay(100)
                    emit(it)
                }

                Timber.tag(TAG).d("Flow complete")
            }

            // The flow itself will not start emitting until we call its terminal operator collect()
            intFlow.collect {
                counter.text = it.toString()
            }
        }
    }

    private fun resetCount() {
        counter.text = "0"
    }
}
