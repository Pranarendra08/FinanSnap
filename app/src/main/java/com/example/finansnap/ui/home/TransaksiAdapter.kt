package com.example.finansnap.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ItemRowTransaksiBinding

class TransaksiAdapter(private val listTransaksi: List<Transaksi>) : RecyclerView.Adapter<TransaksiAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(val binding: ItemRowTransaksiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (listTransaksi[position].tipe == "Pengeluaran") {
            holder.binding.tvKategoriTransaksi.text = listTransaksi[position].kategori
            holder.binding.tvKategoriTransaksi.setTextColor(Color.parseColor("#FD3030"))
            holder.binding.tvTotal.text = listTransaksi[position].total.toString()
            holder.binding.tvTotal.setTextColor(Color.parseColor("#FD3030"))
            holder.binding.tvTanggal.text = listTransaksi[position].tanggal
            holder.binding.tvTanggal.setTextColor(Color.parseColor("#FD3030"))
        }
        if (listTransaksi[position].tipe == "Pemasukan") {
            holder.binding.tvKategoriTransaksi.text = listTransaksi[position].kategori
            holder.binding.tvKategoriTransaksi.setTextColor(Color.parseColor("#21F736"))
            holder.binding.tvTotal.text = listTransaksi[position].total.toString()
            holder.binding.tvTotal.setTextColor(Color.parseColor("#21F736"))
            holder.binding.tvTanggal.text = listTransaksi[position].tanggal
            holder.binding.tvTanggal.setTextColor(Color.parseColor("#21F736"))
        }

//        holder.tvNama.text = listTransaksi[position].username
//        Glide.with(holder.itemView.context)
//            .load(listFavorite[position].avatarUrl)
//            .apply(RequestOptions.circleCropTransform())
//            .into(holder.ivFoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listTransaksi[holder.adapterPosition])

        }
    }

    override fun getItemCount() = listTransaksi.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Transaksi)
    }
}