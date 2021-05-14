package com.example.news.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.Activity.BASE_URL
import com.example.news.Adapter.RecyclerAdapter
import com.example.news.Api.newsAPI_Json
import com.example.news.Interface.apiRequest
import com.example.news.R
import com.google.gson.Gson
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_screen2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Screen2_Fragment : Fragment() {

    lateinit var countDownTimer: CountDownTimer

    private val placeHolderImage = "https://blog.rahulbhutani.com/wp-content/uploads/2020/05/Screenshot-2018-12-16-at-21.06.29.png"
//    private val placeHolderImage = "@drawable/nlogo"
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()
    private var publishData = mutableListOf<String>()
    private var category:String = "Top Stories"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_screen2, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Paper.init(requireContext())
        Log.i("CHECK_CATEGORY",arguments?.getString("message").toString())
        if(arguments?.getString("message").isNullOrEmpty()){
            makeAPIRequest(category)
        }
        else{
            category = arguments?.getString("message").toString()
            makeAPIRequest(category)
        }

    }

    private  fun setUpRecyclerView(){
        rv_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        rv_recyclerView.adapter = RecyclerAdapter(titlesList,descList,imagesList,linksList,publishData)
    }

    @SuppressLint("ResourceType")
    private fun  addToList(title: String, desciption:String, image:String, link:String, publishAt:String){
        titlesList.add(title)
        descList.add(desciption)
        imagesList.add(image)
        linksList.add(link)
        publishData.add(publishAt)
    }

    private fun fadeInFromBlack(){
        v_blackscreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun makeAPIRequest(x: String) {
        progressBar!!.visibility = View.VISIBLE
        val api = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(apiRequest::class.java)
        val cache = Paper.book().read<String>("cache")
        Log.i("SCREEN2","Result = $cache")
        if (cache != null && !cache.isBlank() && cache != null && x=="global news") {
            GlobalScope.launch(Dispatchers.IO){
                try {
                    var response: newsAPI_Json

                    response=Gson().fromJson<newsAPI_Json>(cache, Screen2_Fragment::class.java)
                    for (article in response.articles){
                        Log.i("SCREEN2","Result = $article.content")
                        if(article.urlToImage==null){
                            addToList(article.title,article.description, placeHolderImage,article.url,article.publishedAt)
                        }
                        else
                        {
                            addToList(article.title,article.description,article.urlToImage,article.url,article.publishedAt)
                        }
                    }
                    withContext(Dispatchers.Main){
                        setUpRecyclerView()
                        fadeInFromBlack()
                        progressBar!!.visibility = View.GONE
                    }
                }
                catch (e:Exception){
                    Log.e("Dashboard",e.toString())
                    withContext(Dispatchers.Main){
                        attemptRequestAgain(x)
                    }
                }
            }
        }
        else{
            GlobalScope.launch(Dispatchers.IO){
                try {
                    var response: newsAPI_Json

                    when(x){
                        "Top Stories" -> {
                            response = api.getGeneralNews()
                            Paper.book().write("cache", Gson().toJson(response))
                        }
                        "Business" -> {
                            response = api.getBusinesNews()
                        }
                        "Health" -> {
                            response = api.getHealthNews()
                        }
                        "Entertainment" -> {
                            response = api.getEntertainmentNews()
                        }
                        "Technology" -> {
                            response = api.getTechNews()
                        }
                        "Science" -> {
                            response = api.getScienceNews()
                        }
                        "Sports" -> {
                            response = api.getSportsNews()
                        }
                        else -> {
                            response = api.getNews()
                        }
                    }
                    for (article in response.articles){
                        Log.i("Dashboard","Result = $article.content")
                        if(article.urlToImage==null){
                            addToList(article.title,article.description, placeHolderImage,article.url,article.publishedAt)
                        }
                        else
                        {
                            addToList(article.title,article.description,article.urlToImage,article.url,article.publishedAt)
                        }
                    }
                    withContext(Dispatchers.Main){
                        setUpRecyclerView()
                        fadeInFromBlack()
                        progressBar!!.visibility = View.GONE
                    }
                }
                catch (e:Exception){
                    Log.e("Dashboard",e.toString())
                    withContext(Dispatchers.Main){
                        attemptRequestAgain(x)
                    }
                }
            }
        }
    }

    private  fun attemptRequestAgain(x:String){
        countDownTimer = object :CountDownTimer(5*1000,1000){
            override fun onTick(p0: Long) {
                Toast.makeText(requireContext(),"No Internet!! Trying again...", Toast.LENGTH_SHORT)
            }
            override fun onFinish() {
                try {
                    makeAPIRequest(x)
                    countDownTimer.cancel()
                }
                catch (e : Exception){
                    Log.e("Exception",e.toString())
                }
            }
        }
        countDownTimer.start()
    }
}