package com.example.news.Activity

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.Adapter.ListSourceAdapter
import com.example.news.Common.Common
import com.example.news.Interface.NewsService
import com.example.news.Model.Website
import com.example.news.R
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  ChannelNewsActivity : AppCompatActivity() {

    private  lateinit var mAuth:FirebaseAuth
    lateinit var layoutManager: LinearLayoutManager
    lateinit var mService: NewsService
    lateinit var adapter: ListSourceAdapter
    lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

//        mAuth= FirebaseAuth.getInstance()
//        val currentUser=mAuth.currentUser

        //Init Cache Db
        Paper.init(this)
        //Init Service
        mService = Common.newsService
        back_btn.setOnClickListener {
            super.onBackPressed()
        }
        //init view
        swipe_to_refresh.setOnClickListener{
            loadWebSiteService(true)
        }
        recycler_view_source_news.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recycler_view_source_news.layoutManager = layoutManager

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()

        loadWebSiteService(false)
//        idTxt.text=currentUser?.uid
//        nameTxt.text="Hello "+currentUser?.displayName
//        Glide.with(this).load(currentUser?.photoUrl).into(picView);
    }

    private fun loadWebSiteService(isRefresh: Boolean) {
        if (!isRefresh) {
            val cache = Paper.book().read<String>("cache")
            if (cache != null && !cache.isBlank() && cache != null) {
                /*Read cache*/
                val webSite = Gson().fromJson<Website>(cache, Website::class.java)
                adapter = ListSourceAdapter(baseContext, webSite)
                adapter.notifyDataSetChanged()
                recycler_view_source_news.adapter = adapter
            }
            else {
                /*Load website and write cache*/
                dialog.show()

                /*Fetch new Data*/
                mService.sources.enqueue(object : Callback<Website> {
                    override fun onFailure(call: Call<Website>, t: Throwable) {
                        Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Website>, response: Response<Website>) {
                        adapter = ListSourceAdapter(baseContext, response.body()!!)
                        adapter.notifyDataSetChanged()
                        recycler_view_source_news.adapter = adapter

                        /*Save to cache*/
                        Paper.book().write("cache", Gson().toJson(response.body()))

                        dialog.dismiss()
                    }

                })
            }
        }
        else {
            swipe_to_refresh.isRefreshing = true
            /*Fetch new Data*/
            mService.sources.enqueue(object : Callback<Website> {
                override fun onFailure(call: Call<Website>, t: Throwable) {
                    Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Website>, response: Response<Website>) {
                    adapter = ListSourceAdapter(baseContext, response.body()!!)
                    adapter.notifyDataSetChanged()

                    recycler_view_source_news.adapter = adapter

                    /*Save to cache*/
                    Paper.book().write("cache", Gson().toJson(response.body()))

                    swipe_to_refresh.isRefreshing = false
                }

            })
        }
    }
}