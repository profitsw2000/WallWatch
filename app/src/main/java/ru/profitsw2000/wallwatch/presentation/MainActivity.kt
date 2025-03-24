package ru.profitsw2000.wallwatch.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.profitsw2000.updatescreen.presentation.view.UpdateTimeFragment
import ru.profitsw2000.wallwatch.R
import ru.profitsw2000.wallwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.fragment_container, UpdateTimeFragment.newInstance())
                .commit()
        }
    }
}