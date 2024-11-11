package com.example.superhero.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.superhero.R
import com.example.superhero.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val service = RetrofitProvider.getRetrofit()


        CoroutineScope(Dispatchers.IO).launch {
            val result = service.findSuperheroesByName("super")
            println(result)
        }
    }
}