package com.example.finansnap.ui.input

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finansnap.databinding.FragmentInputBinding
import com.example.finansnap.ui.expense.ExpenseActivity
import com.example.finansnap.ui.income.IncomeActivity

class InputTransaksiFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnPemasukan.setOnClickListener {
            startActivity(Intent(requireActivity(), IncomeActivity::class.java))
        }

        binding.btnPengeluaran.setOnClickListener {
            startActivity(Intent(requireActivity(), ExpenseActivity::class.java))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}