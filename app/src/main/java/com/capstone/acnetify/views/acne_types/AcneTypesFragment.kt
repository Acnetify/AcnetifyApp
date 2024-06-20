package com.capstone.acnetify.views.acne_types

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.acnetify.R
import com.capstone.acnetify.data.model.AcneTypesModel
import com.capstone.acnetify.databinding.FragmentAcneTypesBinding

class AcneTypesFragment : Fragment() {

    private var _binding: FragmentAcneTypesBinding? = null
    private val binding get() =_binding!!
    private val acneTypesAdapter = AcneTypesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentAcneTypesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.rvAcneTypes.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = acneTypesAdapter
        }

        // Provide mock data
        acneTypesAdapter.submitList(getMockData())
    }

    private fun getMockData(): List<AcneTypesModel> {
        return listOf(
                AcneTypesModel(
                    "acne_nodules",
                    R.drawable.acne_nodule,
                    getString(R.string.acne_nodule_desc)
                ),
            AcneTypesModel(
                "milia",
                R.drawable.milia,
                getString(R.string.acne_milia_desc)
            ),
            AcneTypesModel(
                "blackhead",
                R.drawable.blackheads,
                getString(R.string.acne_blackhead_desc)
            ),
            AcneTypesModel(
                "whitehead",
                R.drawable.whiteheads,
                getString(R.string.acne_whitehead_desc)
            ),
            AcneTypesModel(
                "papula_pustula",
                R.drawable.papula_pustula,
                getString(R.string.acne_papula_pustula_desc)
            ),
        )
    }
}