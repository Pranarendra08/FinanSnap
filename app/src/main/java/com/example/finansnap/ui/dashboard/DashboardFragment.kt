package com.example.finansnap.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finansnap.databinding.FragmentDashboardBinding
import com.example.finansnap.ui.expense.ExpenseActivity
import com.example.finansnap.ui.income.IncomeActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
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