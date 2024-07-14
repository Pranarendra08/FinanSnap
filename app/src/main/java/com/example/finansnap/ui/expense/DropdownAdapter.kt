package com.example.finansnap.ui.expense

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.finansnap.R

class DropdownAdapter(context: Context, items: List<DropdownItem>) : ArrayAdapter<DropdownItem>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.dropdown_item, parent, false)
        val item = getItem(position)

        val text = view.findViewById<TextView>(R.id.item_text)

        if (item != null) {
            text.text = item.text
        }

        return view
    }
}