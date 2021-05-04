package com.example.news.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.Common.ISO8601DateParser
import com.example.news.R
import com.example.news.Remote.NewsDetailsActivity
import kotlinx.android.synthetic.main.news_item_layout.view.*
import java.text.ParseException
import java.util.*

class RecyclerAdapter(
    private var titles: List<String>,
    private var details: List<String>,
    private var images: List<String>,
    private var links: List<String>,
    private var publishInfo: List<String>
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        var itemPublish: TextView = itemView.tv_publish
        //takes care of click events
        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = adapterPosition

                val intent = Intent(itemView.context,NewsDetailsActivity::class.java)
                intent.putExtra("webURL", links[position])
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                intent.data = Uri.parse(links[position])
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
        if (publishInfo[position] != null) {
            var date: Date? = null
            try {
                date = ISO8601DateParser.parse(publishInfo[position]!!)
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
//            holder.itemPublish.setReferenceTime(date!!.time)
            holder.itemPublish.text = "Published At: " + date!!.toString()
        }
        Glide.with(holder.itemPicture)
            .load(images[position])
            .into(holder.itemPicture)
    }
}