package com.example.trigmvvm.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trigmvvm.MainActivity
import com.example.trigmvvm.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * An example full-screen fragment that shows a welcome message
 */
class SplashFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var fullscreenContent: View? = null

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Take instance of Action Bar
        // using getSupportActionBar and
        // if it is not Null
        // then call hide function
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullscreenContent = view.findViewById(R.id.fullscreen_content)

        launch {
            delay(3000)
            withContext(Dispatchers.Main){
                (activity as MainActivity).moveToNext()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fullscreenContent = null
    }
}