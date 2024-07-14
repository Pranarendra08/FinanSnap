package com.example.finansnap.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finansnap.R
import com.example.finansnap.databinding.FragmentAdviceBinding

class AdviceFragment : Fragment() {

    private var _binding: FragmentAdviceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val list = ArrayList<Saran>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdviceBinding.inflate(inflater, container, false)
        val root: View = binding.root


        list.addAll(getListSaran())
        showRecyclerList()

        return root
    }

    private fun showRecyclerList() {
        binding.rvSaran.layoutManager = LinearLayoutManager(requireActivity())
        val listJudulAdapter = ListSaranAdapter(requireActivity(), list)
        binding.rvSaran.adapter = listJudulAdapter
    }

    private fun getListSaran(): ArrayList<Saran> {
        val dataJudul = resources.getStringArray(R.array.data_saran)
        val dataLink = resources.getStringArray(R.array.data_link_saran)
        val listSaran = ArrayList<Saran>()
        for (i in dataJudul.indices) {
            val saran = Saran(dataJudul[i], dataLink[i])
            listSaran.add(saran)
        }
        return listSaran
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}