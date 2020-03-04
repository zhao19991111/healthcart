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
class Update : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        // Navigation of buttons
        val updateBtn = view.findViewById<Button>(R.id.update_info)
        val navigateToHome = Navigation.createNavigateOnClickListener(R.id.action_update_to_home)
        updateBtn.setOnClickListener(navigateToHome)
        return view
    }


}
