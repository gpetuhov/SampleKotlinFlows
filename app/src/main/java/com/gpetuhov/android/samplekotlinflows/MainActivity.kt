package com.gpetuhov.android.samplekotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

@FlowPreview    // currently Kotlin Flows are experimental, so we need this annotation
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private var intFlow: Flow<Int>

    init {
        // The flow itself will not start emitting,
        // until we call its terminal operator (for example, collect).
        val flow = flow {
            Timber.tag(TAG).d("Start flow")

            (0..10).forEach {
                delay(500)
                emit(it)
            }

            Timber.tag(TAG).d("Flow complete")
        }

        intFlow = flow
            .filter { it % 2 == 0 }
            .map { it * it }
            .onEach { Timber.tag(TAG).d("Emitting $it") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_button.setOnClickListener { startCount() }
        reset_button.setOnClickListener { resetCount() }
    }

    // === Private methods ===

    private fun startCount() {
        // GlobalScope is used here for simplicity,
        // but activity scope should be used instead.
        GlobalScope.launch(Dispatchers.Main) {
            // We can define our flow once and call collect() multiple times
            // (here - on every button click).
            intFlow.collect { counter.text = it.toString() }
        }
    }

    private fun resetCount() {
        counter.text = "0"
    }
}
