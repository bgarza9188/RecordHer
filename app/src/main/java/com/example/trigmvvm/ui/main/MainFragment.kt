package com.example.trigmvvm.ui.main

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.trigmvvm.CustomAdapter
import com.example.trigmvvm.MainActivity
import com.example.trigmvvm.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.io.IOException

val FILE_EXTENSION = ".mp4"
val FORWARD_SLASH = "/"

class MainFragment : Fragment() {
    private var saveDirectoryPath: String = ""

    private var recorder: MediaRecorder? = null

    private var player: MediaPlayer? = null

    private var adapter = CustomAdapter()

    private var fileNumberSeed = 99

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Take instance of Action Bar
        // using getSupportActionBar and
        // if it is not Null
        // then call hide function
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).initTheme()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)


        // Record to the external cache directory for visibility
        saveDirectoryPath = "${context?.externalCacheDir?.absolutePath}" + FORWARD_SLASH + "audiorecordtest" + FILE_EXTENSION

        //for now get list of files found in teh cache dir and add them to the apdater
        context?.externalCacheDir?.absoluteFile?.list()?.forEach {
            Log.d("ben", "file: $it")
            adapter.add(it)
        }
        fileNumberSeed = adapter.itemCount

        val recyclerView = view.recycler_view
        recyclerView.adapter = adapter

        return view
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(saveDirectoryPath)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("ben", "prepare() failed")
            }
        }
    }

    private fun generateFileNameWithPath(): String {
        return """${context?.externalCacheDir?.absolutePath}${FORWARD_SLASH}audioRecordTest${fileNumberSeed++}$FILE_EXTENSION"""
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            generateFileNameWithPath().also {
                setOutputFile(it)
                adapter.add(it)
                adapter.notifyDataSetChanged()
            }
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