package com.capstone.acnetify.views.acne_types

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
            AcneTypesModel("Acne Type A","https://raw.githubusercontent.com/kisahtegar/machine-learning-android/barcode-scanning/preview.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris\n" +
                    "        volutpat, dolor id interdum ullamcorper, risus dolor egestas lectus, sit amet mattis purus\n" +
                    "        dui nec risus. Maecenas non sodales nisi, vel dictum dolor."),
            AcneTypesModel("Acne Type B","https://raw.githubusercontent.com/kisahtegar/machine-learning-android/barcode-scanning/preview.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris\n" +
                    "        volutpat, dolor id interdum ullamcorper, risus dolor egestas lectus, sit amet mattis purus\n" +
                    "        dui nec risus. Maecenas non sodales nisi, vel dictum dolor."),
            AcneTypesModel("Acne Type C","https://raw.githubusercontent.com/kisahtegar/machine-learning-android/barcode-scanning/preview.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris\n" +
                    "        volutpat, dolor id interdum ullamcorper, risus dolor egestas lectus, sit amet mattis purus\n" +
                    "        dui nec risus. Maecenas non sodales nisi, vel dictum dolor."),
            AcneTypesModel("Acne Type D","https://raw.githubusercontent.com/kisahtegar/machine-learning-android/barcode-scanning/preview.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris\n" +
                    "        volutpat, dolor id interdum ullamcorper, risus dolor egestas lectus, sit amet mattis purus\n" +
                    "        dui nec risus. Maecenas non sodales nisi, vel dictum dolor."),
            AcneTypesModel("Acne Type E","https://raw.githubusercontent.com/kisahtegar/machine-learning-android/barcode-scanning/preview.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris\n" +
                    "        volutpat, dolor id interdum ullamcorper, risus dolor egestas lectus, sit amet mattis purus\n" +
                    "        dui nec risus. Maecenas non sodales nisi, vel dictum dolor."),
            AcneTypesModel("Acne Type E","https://raw.githubusercontent.com/kisahtegar/machine-learning-android/barcode-scanning/preview.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam in scelerisque sem. Mauris\n" +
                    "        volutpat, dolor id interdum ullamcorper, risus dolor egestas lectus, sit amet mattis purus\n" +
                    "        dui nec risus. Maecenas non sodales nisi, vel dictum dolor."),
        )
    }
}