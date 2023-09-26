package com.example.stopwatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityMainBinding
import com.example.stopwatch.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.startCollect()

        binding.buttonStart.setOnClickListener {
            viewModel.startClicked()
        }
        binding.buttonPause.setOnClickListener {
            viewModel.pauseClicked()
        }
        binding.buttonStop.setOnClickListener {
            viewModel.stopClicked()
        }
    }

    private fun initViewModel() {
        viewModel = MainActivityViewModel().apply {
            subscribe().observe(this@MainActivity, { setString(it) })
        }
    }

    private fun setString(string: String?) {
        binding.textTime.text = string
    }
}