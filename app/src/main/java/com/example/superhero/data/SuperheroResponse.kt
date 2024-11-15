package com.example.superhero.data

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.example.superhero.R
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

data class SuperheroResponse (
    @SerializedName ("response") val response: String,
    @SerializedName ("results-for") val resultsFor: String,
    @SerializedName ("results") val results: List<Superhero>
) { }

data class Superhero (
    @SerializedName("id") val id:String,
    @SerializedName("name") val name:String,
    @SerializedName("powerstats") val stats:Stats,
    @SerializedName("biography") val biography:Biography,
    @SerializedName("appearance") val appearance:Appearance,
    @SerializedName("work") val work:Work,
    @SerializedName("image") val image:Image
) {
    @ColorRes
    fun getAlignmentColor() : Int {
        return when (biography.alignment) {
            "good" -> R.color.alignment_color_good
            "bad" -> R.color.alignment_color_bad
            else -> R.color.alignment_color_neutral
        }
    }
}

data class Stats (
    @JsonAdapter(IntegerAdapter::class) @SerializedName("intelligence") val intelligence: Int,
    @JsonAdapter(IntegerAdapter::class) @SerializedName("strength") val strength: Int,
    @JsonAdapter(IntegerAdapter::class) @SerializedName("speed") val speed: Int,
    @JsonAdapter(IntegerAdapter::class) @SerializedName("durability") val durability: Int,
    @JsonAdapter(IntegerAdapter::class) @SerializedName("power") val power: Int,
    @JsonAdapter(IntegerAdapter::class) @SerializedName("combat") val combat: Int,
) { }

data class Biography (
    @SerializedName("full-name") val realName:String,
    @SerializedName("place-of-birth") val placeOfBirth:String,
    @SerializedName("alignment") val alignment:String,
    @SerializedName("publisher") val publisher:String
) { }

data class Appearance (
    @SerializedName("race") val race:String,
) { }

data class Work (
    @SerializedName("occupation") val occupation:String,
    @SerializedName("base") val base:String
) { }

data class Image (
    @SerializedName("url") val url:String
) { }


class IntegerAdapter : TypeAdapter<Int>() {
    override fun write(out: JsonWriter?, value: Int) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader?): Int {
        if (`in` != null) {
            val value: String = `in`.nextString()
            if (value != "null") {
                return value.toInt()
            }
        }
        return 0
    }

}