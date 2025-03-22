package ru.profitsw2000.wallwatch.presentation

import android.app.ActionBar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.profitsw2000.wallwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}