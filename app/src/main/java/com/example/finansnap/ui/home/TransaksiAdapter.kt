package com.example.finansnap.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finansnap.R
import com.example.finansnap.database.Transaksi
import com.example.finansnap.databinding.ItemRowTransaksiBinding
import com.example.finansnap.util.withCurrencyFormat
import com.example.finansnap.util.withDateFormat

class TransaksiAdapter(private var listTransaksi: List<Transaksi>) : RecyclerView.Adapter<TransaksiAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun updateData(newList: List<Transaksi>) {
        listTransaksi = newList
        notifyDataSetChanged()
    }

    class ListViewHolder(val binding: ItemRowTransaksiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (listTransaksi[position].tipe == "Pengeluaran") {
            holder.binding.tvKategoriTransaksi.text = listTransaksi[position].kategori
            holder.binding.tvTotal.text = listTransaksi[position].total.toString().withCurrencyFormat()
            holder.binding.tvTotal.setTextColor(Color.parseColor("#FD3030"))
            holder.binding.tvTanggal.text = listTransaksi[position].tanggal.withDateFormat()
            holder.binding.imageView4.setImageResource(R.drawable.ic_arrow_downward_expense)
        }
        if (listTransaksi[position].tipe == "Pemasukan") {
            holder.binding.tvKategoriTransaksi.text = listTransaksi[position].kategori
            holder.binding.tvTotal.text = listTransaksi[position].total.toString().withCurrencyFormat()
            holder.binding.tvTotal.setTextColor(Color.parseColor("#21F736"))
            holder.binding.tvTanggal.text = listTransaksi[position].tanggal.withDateFormat()
            holder.binding.imageView4.setImageResource(R.drawable.ic_arrow_upward_income)
        }
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listTransaksi[holder.adapterPosition])
        }
    }

    override fun getItemCount() = listTransaksi.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Transaksi)
    }
}