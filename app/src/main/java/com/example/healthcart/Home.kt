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
class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Navigation of buttons
        val updateBtn = view.findViewById<Button>(R.id.update)
        val selectBtn = view.findViewById<Button>(R.id.select)
        val navigateToUpdate = Navigation.createNavigateOnClickListener(R.id.action_home_to_update)
        val navigateToSelect = Navigation.createNavigateOnClickListener(R.id.action_home_to_select)
        updateBtn.setOnClickListener(navigateToUpdate)
        selectBtn.setOnClickListener(navigateToSelect)
        return view
    }


}
