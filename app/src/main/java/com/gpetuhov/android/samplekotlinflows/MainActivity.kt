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
        // Here we just define flow.
        // The flow itself will not start emitting,
        // until we call its terminal operator (for example, collect).
        val flow = flow {
            Timber.tag(TAG).d("Start flow")

            (0..10).forEach {
                // Emit items with 500 milliseconds delay
                delay(500)
                emit(it)
            }

            Timber.tag(TAG).d("Flow complete")
        }

        // We can transform flow by calling filter, map and other operators
        // (just like RxJava Observable).
        intFlow = flow
            .filter { it % 2 == 0 }
            .map { it * it }
            .onEach { Timber.tag(TAG).d("Emitting $it on ${Thread.currentThread()}") }
            // Everything, that is above this operator, will run on background thread
            // in computational (Default) thread pool.
            // Without this operator, everything will run on the main thread.
            .flowOn(Dispatchers.Default)
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
            // This is where our flow starts emitting items.
            // We can define our flow once and call collect() multiple times
            // (here - on every button click).
            intFlow.collect {
                // This runs on the main thread,
                // because Dispatchers.Main is used in launch coroutine builder.
                Timber.tag(TAG).d("Collecting $it on ${Thread.currentThread()}")
                counter.text = it.toString()
            }
        }
    }

    private fun resetCount() {
        counter.text = "0"
    }
}
