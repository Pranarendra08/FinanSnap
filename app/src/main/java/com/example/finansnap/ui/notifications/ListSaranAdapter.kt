package com.example.finansnap.ui.notifications

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finansnap.R
import com.example.finansnap.databinding.ItemRowSaranBinding

class ListSaranAdapter(private val context: Context, private val listSaran: ArrayList<Saran>) : RecyclerView.Adapter<ListSaranAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ItemRowSaranBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowSaranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (judul, link) = listSaran[position]
        holder.binding.tvJudulSaran.text = judul
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listSaran.size
    }


}