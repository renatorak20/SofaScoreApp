package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentBasketballBinding
import com.example.sofascoreapp.databinding.FragmentFootballBinding

class BasketballFragment : Fragment() {

    private lateinit var binding: FragmentBasketballBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketballBinding.inflate(layoutInflater)

        return binding.root
    }


}