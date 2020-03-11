package com.example.healthcart


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

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
        val weight = view.findViewById<EditText>(R.id.weight)
        val height = view.findViewById<EditText>(R.id.height)
        val age = view.findViewById<EditText>(R.id.age)
        var weight_val = 0.0
        var height_val = 0.0
        var age_val = 0
        val weight_spin = view.findViewById<Spinner>(R.id.weight_spinner)
        val height_spin = view.findViewById<Spinner>(R.id.height_spinner)
        val w_unit = weight_spin.selectedItem.toString()
        val h_unit = height_spin.selectedItem.toString()

        updateBtn.setOnClickListener{
            try {
                weight_val = weight.text.toString().toDouble()
            }
            catch(e: Exception)
            {
                Toast.makeText(
                    getActivity(),
                    "Invalid Weight Entry",
                    Toast.LENGTH_LONG
                ).show()
            }
            try {
                height_val = height.text.toString().toDouble()
            }
            catch(e: Exception)
            {
                Toast.makeText(
                    getActivity(),
                    "Invalid Height Entry",
                    Toast.LENGTH_LONG
                ).show()
            }
            try
            {
                age_val = age.text.toString().toInt()
            }
            catch(e: Exception){
                Toast.makeText(
                    getActivity(),
                    "Invalid Age Entry",
                    Toast.LENGTH_LONG
                ).show()
            }
            saveStrToDB("weight", weight_val.toString())
            saveStrToDB("height", height_val.toString())
            saveStrToDB("age", age_val.toString())
            saveStrToDB("weight_unit", w_unit)
            saveStrToDB("height_unit", h_unit)
            findNavController().navigate(UpdateDirections.actionUpdateToHome())
        }
        return view
    }

    private fun saveStrToDB( key: String, str: String): Unit
    {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, str)
            commit()
        }
    }


}
