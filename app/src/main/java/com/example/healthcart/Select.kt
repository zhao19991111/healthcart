package com.example.healthcart


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import androidx.navigation.Navigation
import android.webkit.WebViewClient
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import java.util.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_select.*


/**
 * A simple [Fragment] subclass.
 */
class Select : Fragment() {

    private var food_list = arrayListOf<String> ()
    private var data_list = arrayListOf<ArrayList<String>> ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select, container, false)
        // Navigation of buttons
        val recordBtn = view.findViewById<Button>(R.id.record)
        val returnBtn = view.findViewById<Button>(R.id.return_btn)
        val confirmBtn = view.findViewById<Button>(R.id.confirm_btn)
        val cancelBtn = view.findViewById<Button>(R.id.cancel_btn)
        val popupBox = view.findViewById<ConstraintLayout>(R.id.popup_block)
        // Open the ucla dining website
        val dining_website: WebView = view.findViewById(R.id.ucla_dining)
        val entry_list = arrayOf(R.id.food_name0, R.id.food_name1, R.id.food_name2, R.id.food_name3, R.id.food_name4, R.id.food_name5, R.id.food_name6, R.id.food_name7, R.id.food_name8)
        val button_list = arrayOf(R.id.cancel_btn0, R.id.cancel_btn1, R.id.cancel_btn2, R.id.cancel_btn3, R.id.cancel_btn4, R.id.cancel_btn5, R.id.cancel_btn6, R.id.cancel_btn7, R.id.cancel_btn8)
        val row_list = arrayOf(R.id.row0, R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6, R.id.row7, R.id.row8)
        dining_website.settings.javaScriptEnabled = true
        dining_website.loadUrl("http://menu.dining.ucla.edu/Menus")
        // Go back to the menu website if returned
        returnBtn.setOnClickListener {
            popupBox.visibility = View.INVISIBLE
            dining_website.loadUrl("http://menu.dining.ucla.edu/Menus")
        }
        dining_website.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url:String): Boolean {
                view.loadUrl(url)
                return false // then it is not handled by default action
            }
        })
        // get the data from html
        lateinit var url_list: kotlin.collections.List<String>;
        recordBtn.setOnClickListener {
            url_list = dining_website.getUrl().split("/").map{it}
            if (url_list[3] == "Menus")
            {
                Log.e("Status", "Menu")
                Toast.makeText(getActivity(),  "Hint: Choose your favorite Dining Hall and click \"Nutritive Analysis\"", Toast.LENGTH_LONG).show()
            }
            else if (url_list[3] == "NutritiveAnalysis")
            {
                if (url_list[4] != "Report") {
                    Log.e("Status", "Analysis")
                    dining_website.evaluateJavascript(
                        "(function() { \n"
                                + " if (document.getElementsByClassName(\"sel-name\").length === 0)\n"
                                + " {return false;} \n"
                                + " else {return true;} })()"
                    ) { value ->
                        run {
                            Log.e("Value", value)
                            if (value == "true") {
                                dining_website.evaluateJavascript("(function() {document.getElementById(\"button-analyze\").click()})()") {}
                            } else {
                                Toast.makeText(
                                    getActivity(),
                                    "Hint: Choose food you want by clicking \"+\", click \"continue\" when finished",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                else
                {
                        dining_website.evaluateJavascript("(function() {document.getElementById(\"showdetail\").click()})()") { value ->
                            run {
                                dining_website.evaluateJavascript(
                                    "(function() {"
                                            + " var items = document.getElementsByClassName(\"rpt-item\") \n"
                                            + " var data = document.getElementsByClassName(\"rpt-data\") \n"
                                            + " var return_arr = []\n"
                                            + " for (let i=0; i<items.length; i++)\n"
                                            + " {  return_arr.push(items[i].innerHTML) \n"
                                            + "    for (let j=0; j<6; j++) \n"
                                            + "    { return_arr.push(data[j+i*7].innerHTML) } \n"
                                            + " } return return_arr\n"
                                            + "})()"
                                ) { value ->
                                    run {
                                        Log.e("Result", value)
                                        recordBtn.visibility = View.INVISIBLE
                                        processValue(value)
                                        if (food_list.size > 9) // too many items selected
                                        {
                                            Toast.makeText(
                                                getActivity(),
                                                "Please choose less than 9 items",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            recordBtn.visibility = View.VISIBLE
                                        }
                                        else {
                                            popupBox.visibility = View.VISIBLE
                                            entry_list.forEachIndexed { index, element ->
                                                view.findViewById<TableRow>(row_list[index])
                                                    .visibility = View.INVISIBLE
                                            }
                                            cancelBtn.setOnClickListener {
                                                popupBox.visibility = View.INVISIBLE
                                                recordBtn.visibility = View.VISIBLE
                                            }
                                            confirmBtn.setOnClickListener {
                                                run {
                                                    saveStrToDB("new_food_list", arrayToString(food_list))
                                                    saveStrToDB("new_data_list", array2ToString(data_list))
                                                    findNavController().navigate(
                                                            SelectDirections.actionSelectToList(
                                                            arrayToString(food_list),
                                                            array2ToString(data_list)
                                                        )
                                                    )
                                                }
                                            }
                                            food_list.forEachIndexed { ind, ele
                                                ->
                                                run {
                                                    view.findViewById<TextView>(entry_list[ind])
                                                        .text = ele
                                                    view.findViewById<TableRow>(row_list[ind])
                                                        .visibility = View.VISIBLE
                                                    // update (delete a food item)
                                                    view.findViewById<Button>(button_list[ind])
                                                        .setOnClickListener {
                                                            view.findViewById<TableRow>(row_list[ind])
                                                                .visibility = View.GONE
                                                            var temp_food_list =
                                                                arrayListOf<String>()
                                                            var temp_data_list =
                                                                arrayListOf<ArrayList<String>>()
                                                            food_list.forEachIndexed { i, e ->
                                                                run {
                                                                    if (i != ind) {
                                                                        temp_food_list.add(e)
                                                                        temp_data_list.add(data_list[i])
                                                                        Log.e("val:", e)
                                                                    }
                                                                }
                                                            }
                                                            food_list = temp_food_list
                                                            data_list = temp_data_list
                                                        }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }
        return view
    }

    private fun processValue(value: String): Unit
    {
        food_list.clear()
        data_list.clear()
        val pre_list: kotlin.collections.List<String> = value.replace("[", "").replace("&amp;", "&")
            .replace("]", "").split("\",\"").map{it.replace("\"","").replace("NaN", "0").trim()}
        val summary_list = arrayListOf<String>()
        pre_list.forEach {
            ele -> run {
              if (ele != "," && ele != "")
                  summary_list.add(ele.replace("\\\"","\"").trim())
            }
        }
        for (i in 0..(summary_list.size / 7))
        {
            data_list.add(arrayListOf<String>())
        }
        summary_list.forEachIndexed { index, ele ->
            run {
                if (index % 7 == 0) {
                    food_list.add(ele)
                } else {
                    data_list[index / 7].add(ele)
                }
            }
        }
        food_list.forEach {
            ele -> Log.e("Food", ele)
        }
        data_list.forEachIndexed {
            index, ele -> ele.forEach {e -> Log.e("Data $index", e)}
        }
    }

    private fun saveStrToDB(key: String, str: String): Unit
    {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, str)
            commit()
        }
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
