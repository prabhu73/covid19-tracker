package com.covidtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.main_nav)
    }
}
