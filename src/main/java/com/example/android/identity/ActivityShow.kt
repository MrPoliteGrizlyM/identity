package com.example.android.identity

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import android.content.Context
import android.content.Intent
import android.os.Looper

import android.support.v7.app.AlertDialog
import android.text.Html
import android.util.Log

import kotlinx.android.synthetic.main.activity_show.*
import okhttp3.*
import org.jetbrains.anko.toast
import java.io.IOException
import kotlin.math.log
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import org.json.JSONObject
import java.util.*


class ActivityShow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.content_activity_show)

        val iin = intent.getStringExtra("message")


        fetchJSON(iin)


    }


    fun fetchJSON(iin: String){

        val url = "https://identity-web.herokuapp.com/api/persons?api_key=r8YbJG2aZExGkX5h&uid=$iin"

        val request = Request.Builder().url(url).build()


        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{

            override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        toast("No internet connection, please try again. $e")
                    }
                    finish()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()

                val person = JSONObject(body)
                val profile = person.getString("response")
                val relatives = person.getJSONObject("response").getString("relatives")

                val profiles: List<Profile> = gson.fromJson("[$profile]", Array<Profile>::class.java).toList()
                val relatives_list: List<Relatives> = gson.fromJson(relatives, Array<Relatives>::class.java).toList()

//                    if (profile == iin) {
                         runOnUiThread {
                             findViewById<TextView>(R.id.name_text).apply {
                                 text =
                                     Html.fromHtml(profiles[0].name)
                             }

                             findViewById<TextView>(R.id.date_birth).apply {
                                 text = Html.fromHtml("<strong>Date of birth:</strong> " + profiles[0].dateOfBirth)
                             }

                             findViewById<TextView>(R.id.feature).apply {
                                 text = Html.fromHtml("<strong>Feature:</strong> " + profiles[0].feature)
                             }

                             findViewById<TextView>(R.id.address_text).apply {
                                 text = Html.fromHtml("<strong>Address:</strong> " + profiles[0].address)
                             }

                             findViewById<TextView>(R.id.hospital_text).apply {
                                 text = Html.fromHtml("<strong>Hospital:</strong> " + profiles[0].hospital)
                             }

                             findViewById<TextView>(R.id.comments).apply {
                                 text = Html.fromHtml("<strong>Comment:</strong> " + profiles[0].comments)
                             }
                         }

                runOnUiThread {
                    if (findViewById<TextView>(R.id.name_text).text == "") {
                            toast("No such profile")
                            finish()
                    }
                }

            }
        })
    }




}


class Profile(val id: Int, val uid: String, val name: String, val dateOfBirth: String,
              val feature: String, val address: String, val hospital: String,
              val comments: String)

class Relatives(val id: Int, val name: String, val address: String, val comment: String)



