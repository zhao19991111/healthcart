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
class List : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        // Navigation of buttons
        val finishBtn = view.findViewById<Button>(R.id.finish)
        val reselectBtn = view.findViewById<Button>(R.id.reselect)
        val navigateToHome = Navigation.createNavigateOnClickListener(R.id.action_list_to_home)
        val navigateToSelect = Navigation.createNavigateOnClickListener(R.id.action_list_to_select)
        finishBtn.setOnClickListener(navigateToHome)
        reselectBtn.setOnClickListener(navigateToSelect)
        return view
    }


}
