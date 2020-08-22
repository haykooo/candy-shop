package com.example.candyshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_notifications, R.id.navigation_menu))

        navView.setupWithNavController(navController)

        val navMenuView = navView.getChildAt(0) as BottomNavigationMenuView
        val view = navMenuView.getChildAt(2)

        val itemView = view as BottomNavigationItemView
        val customIcon: View = LayoutInflater.from(this).inflate(R.layout.bottom_nav_bar_center_cart_item, navView, false)

        itemView.addView(customIcon)
    }
}