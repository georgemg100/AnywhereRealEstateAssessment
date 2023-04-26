package com.example.assesmentanywhererealestate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.assesmentanywhererealestate.R
import com.example.assesmentanywhererealestate.databinding.FragmentDetailsBinding
import com.example.assesmentanywhererealestate.ui.viewmodel.CharactersViewModel
import com.example.assesmentanywhererealestate.ui.viewmodel.DetailsUiState
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!
    val charactersViewModel: CharactersViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        charactersViewModel.characterDetailsLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is DetailsUiState.Success -> {
                    it.iconUrl?.let {pathString ->
                        if(pathString.isNotEmpty()) {
                            Picasso.get().load(pathString).placeholder(R.drawable.baseline_person_24).into(binding.characterImage)
                        } else {
                            binding.characterImage.setImageResource(R.drawable.baseline_person_24)
                        }
                    }
                    binding.textviewTitle.text = it.title
                    binding.textviewFirst.text = it.description
                }
                is DetailsUiState.Loading -> {
                    //show loading indicator
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}