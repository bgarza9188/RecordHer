package com.example.trigmvvm.ui.main

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.trigmvvm.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.IOException


class MainFragment : Fragment() {
    private var fileName: String = ""

    private var recorder: MediaRecorder? = null

    private var player: MediaPlayer? = null




    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)


        // Record to the external cache directory for visibility
        fileName = "${context?.externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        return view
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("ben", "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("ben", "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startRec.setOnClickListener {
            Log.e("ben","start clicked")
            startRecording()
        }

        stopRec.setOnClickListener {
            Log.e("ben", "stop clicked")
            stopRecording()
        }

        startPlay.setOnClickListener {
            Log.e("ben","Start playing")
            startPlaying()
        }

        stopPlay.setOnClickListener {
            Log.e("ben","stop playing")
            stopPlaying()
        }

    }
}