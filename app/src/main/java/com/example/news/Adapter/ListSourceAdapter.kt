package com.example.news.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.Interface.ItemClickListener
import com.example.news.Model.Website
import com.example.news.R
import kotlinx.android.synthetic.main.source_news_layout.view.*

class ListSourceAdapter(val context: Context, val website: Website): RecyclerView.Adapter<ListSourceAdapter.ListSourceViewHolder>() {

    class ListSourceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var itemClickListener: ItemClickListener

        var source_title = itemView.source_news_name
        init {
            itemView.setOnClickListener(this)
        }
        fun setItemClickListener(itemClickListener: ItemClickListener) {
            this.itemClickListener = itemClickListener
        }

        override fun onClick(v: View?) {
            itemClickListener.onClick(v!!, adapterPosition)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSourceViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.source_news_layout, parent, false)
        return ListSourceViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        Log.i("ITEMCCOUNT",website.sources!!.size.toString())
        return website.sources!!.size
    }
    override fun onBindViewHolder(holder: ListSourceViewHolder, position: Int) {
        holder.source_title.text = website.sources!![position].name

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, ListNewsActivity::class.java)
                intent.putExtra("source", website.sources!![position].id)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        })
    }
}