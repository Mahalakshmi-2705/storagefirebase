package com.example.image

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val imageUploadFragment = ImageUploadFragment()
    private val videoUploadFragment = VedioUploadFragment()


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_image_upload -> {
                loadFragment(imageUploadFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_video_upload -> {
                loadFragment(videoUploadFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavView)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        loadFragment(imageUploadFragment) // Load the image upload fragment by default
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
