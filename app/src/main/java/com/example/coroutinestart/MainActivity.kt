package com.example.coroutinestart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.coroutinestart.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLoad.setOnClickListener {
            lifecycleScope.launch {
                loadData()
            }
//            loadDataWithoutCoroutine()
        }
    }

    private suspend fun loadData() {
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false
        val city = loadCity()
        binding.tvLocation.text = city
        val temp = loadTemperature(city)
        binding.tvTemperature.text = temp.toString()
        binding.progress.isVisible = false
        binding.buttonLoad.isEnabled = true
    }

    private fun loadDataWithoutCoroutine(step: Int = 0, obj: Any? = null) {
        when(step) {
            0 -> {
                binding.progress.isVisible = true
                binding.buttonLoad.isEnabled = false
                loadCityWithout {
                    loadDataWithoutCoroutine(1, it)
                }
            }
            1 -> {
                val city = obj as String
                binding.tvLocation.text = city
                loadTemperatureWithout(city) {
                    loadDataWithoutCoroutine(2, it)
                }
            }
            2 -> {
                val temp = obj as Int
                binding.tvTemperature.text = temp.toString()
                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
    }

    private suspend fun loadCity(): String {
        delay(5000)
        return "Sumy"
    }
    private fun loadCityWithout(callback: (String) -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke("Sumy")
        }, 5000)
    }

    private suspend fun loadTemperature(city: String): Int {
        Toast.makeText(
            this,
            getString(R.string.loading_temperature_toast, city),
            Toast.LENGTH_SHORT
        ).show()
        delay(5000)
        return 3
    }
    private fun loadTemperatureWithout(city: String, callback: (Int) -> Unit) {
        Toast.makeText(
            this,
            getString(R.string.loading_temperature_toast, city),
            Toast.LENGTH_SHORT
        ).show()

        Handler(Looper.getMainLooper()).postDelayed({
            callback.invoke(17)
        }, 5000)
    }
}