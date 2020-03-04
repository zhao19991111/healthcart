package com.example.healthcart


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 */
class Detail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_detail, container, false)
        // TO DO:
        // Display a detailed table with columns: food type, protein, carb, fat, calories
        // Construct rows based on the number of food items
        // Get the Highlighted Summary of Nutrients and Calories
        return view
    }


}
