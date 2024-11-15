package com.example.superhero.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.superhero.R
import com.example.superhero.data.Superhero
import com.example.superhero.databinding.ActivityDetailBinding
import com.example.superhero.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SUPERHERO_ID = "SUPERHERO_ID"
    }

    lateinit var binding: ActivityDetailBinding

    lateinit var superhero: Superhero

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navigationBar.setOnItemSelectedListener {
            setSelectedTab(it.itemId)
        }

        binding.navigationBar.selectedItemId = R.id.menu_biography

        val id = intent.getStringExtra(EXTRA_SUPERHERO_ID)!!

        getSuperhero(id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setSelectedTab(itemId: Int) : Boolean {
        when (itemId) {
            R.id.menu_biography -> {
                binding.statsContent.root.visibility = View.GONE
                binding.appearanceContent.root.visibility = View.GONE
                binding.biographyContent.root.visibility = View.VISIBLE
            }
            R.id.menu_appearance -> {
                binding.biographyContent.root.visibility = View.GONE
                binding.statsContent.root.visibility = View.GONE
                binding.appearanceContent.root.visibility = View.VISIBLE
            }
            R.id.menu_stats -> {
                binding.appearanceContent.root.visibility = View.GONE
                binding.biographyContent.root.visibility = View.GONE
                binding.statsContent.root.visibility = View.VISIBLE
            }
        }

        return true
    }

    fun loadData() {
        supportActionBar?.title = superhero.name
        Picasso.get().load(superhero.image.url).into(binding.avatarImageView)

        // Biography
        binding.biographyContent.realNameTextView.text = superhero.biography.realName
        binding.biographyContent.publisherTextView.text = superhero.biography.publisher
        binding.biographyContent.placeOfBirthTextView.text = superhero.biography.placeOfBirth
        binding.biographyContent.alignmentTextView.text = superhero.biography.alignment
        binding.biographyContent.alignmentTextView.setTextColor(getColor(superhero.getAlignmentColor()))
        binding.biographyContent.occupationTextView.text = superhero.work.occupation
        binding.biographyContent.baseTextView.text = superhero.work.base

        // Appearance
        binding.appearanceContent.raceTextView.text = superhero.appearance.race

        // Stats
        binding.statsContent.intelligenceTextView.text = superhero.stats.intelligence.toString()
        binding.statsContent.intelligenceProgressBar.progress = superhero.stats.intelligence
        binding.statsContent.strengthTextView.text = superhero.stats.strength.toString()
        binding.statsContent.strengthProgressBar.progress = superhero.stats.strength
        binding.statsContent.speedTextView.text = superhero.stats.speed.toString()
        binding.statsContent.speedProgressBar.progress = superhero.stats.speed
        binding.statsContent.durabilityTextView.text = superhero.stats.durability.toString()
        binding.statsContent.durabilityProgressBar.progress = superhero.stats.durability
        binding.statsContent.powerTextView.text = superhero.stats.power.toString()
        binding.statsContent.powerProgressBar.progress = superhero.stats.power
        binding.statsContent.combatTextView.text = superhero.stats.combat.toString()
        binding.statsContent.combatProgressBar.progress = superhero.stats.combat
    }

    private fun getSuperhero(id: String) {
        val service = RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                superhero = service.findSuperheroById(id)

                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }
}