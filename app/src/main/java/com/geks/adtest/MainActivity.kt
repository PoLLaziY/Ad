package com.geks.adtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.geks.adtest.databinding.ActivityMainBinding
import com.geks.adtest.refactered.AdManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.nextActivity.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.loadBanner.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                AdManager.loadAd(binding.banner)
            }
        }

        binding.addFrame.setOnClickListener {
            AdManager.lock(this)
        }

        binding.showInter.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                AdManager.navigate(this@MainActivity) {
                    this@MainActivity.startActivity(
                        Intent(
                            this@MainActivity,
                            MainActivity2::class.java
                        )
                    )
                }
            }
        }
    }
}