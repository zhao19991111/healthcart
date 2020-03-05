package com.example.healthcart


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import java.util.ArrayList

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
        val args = ListArgs.fromBundle(arguments!!)
        val food_list = stringToArray(args.foodStr)
        val data_list = stringToArray2(args.dataStr)
        food_list.forEach{
            ele -> Log.e("Food", ele)
        }
        data_list.forEach{
            ele -> ele.forEach {
                e-> Log.e("Data", e)
            }
        }
        return view
    }

    private fun arrayToString (arrList: ArrayList<String>): String
    {
        var finalStr: String = ""
        arrList.forEach {
                ele -> run {
            finalStr += (ele + " | ")
        }
        }
        return finalStr

    }

    private fun stringToArray (str: String): ArrayList<String>
    {
        var finalArr: ArrayList<String> = arrayListOf()
        var tempList = str.split(" | ")
        tempList.forEach{
                ele -> run {
            if (ele != "")
                finalArr.add(ele)
        }
        }
        return finalArr
    }

    private fun array2ToString (arrList: ArrayList<ArrayList<String>>): String
    {
        var finalStr: String = ""
        arrList.forEach{
                ele -> run {
            finalStr += (arrayToString(ele) + " ## ")
        }
        }
        return finalStr
    }

    private fun stringToArray2 (str: String): ArrayList<ArrayList<String>> {
        var finalArr: ArrayList<ArrayList<String>> = arrayListOf<ArrayList<String>>()
        var tempList = str.split(" ## ")
        tempList.forEach{
                ele -> run {
            if (ele != "") {
                finalArr.add(stringToArray(ele))
            }
        }
        }
        return finalArr
    }


}
