package com.example.healthcart


import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import androidx.navigation.Navigation
import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.concurrent.CountDownLatch
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class Select : Fragment() {

    val food_list = arrayListOf<String> ()
    val data_list = arrayListOf<ArrayList<String>> ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select, container, false)
        // Navigation of buttons
        val recordBtn = view.findViewById<Button>(R.id.record)
        val returnBtn = view.findViewById<Button>(R.id.return_btn)
        val confirmBtn = view.findViewById<Button>(R.id.confirm_btn)
        val cancelBtn = view.findViewById<Button>(R.id.cancel_btn)
        val navigateToList = Navigation.createNavigateOnClickListener(R.id.action_select_to_list)
        val popupBox = view.findViewById<ConstraintLayout>(R.id.popup_block)
        // Open the ucla dining website
        val dining_website: WebView = view.findViewById(R.id.ucla_dining)
        dining_website.settings.javaScriptEnabled = true
        dining_website.loadUrl("http://menu.dining.ucla.edu/Menus")
        // Go back to the menu website if returned
        returnBtn.setOnClickListener { dining_website.loadUrl("http://menu.dining.ucla.edu/Menus") }
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
                            dining_website.evaluateJavascript("(function() {"
                                                                    +" var items = document.getElementsByClassName(\"rpt-item\") \n"
                                                                    +" var data = document.getElementsByClassName(\"rpt-data\") \n"
                                                                    +" var return_arr = []\n"
                                                                    +" for (let i=0; i<items.length; i++)\n"
                                                                    +" {  return_arr.push(items[i].innerHTML) \n"
                                                                    +"    for (let j=0; j<6; j++) \n"
                                                                    +"    { return_arr.push(data[j+i*7].innerHTML) } \n"
                                                                    +" } return return_arr\n"
                                                                    +"})()") { value ->
                                run {
                                    Log.e("Result", value)
                                    processValue(value)
                                }
                            }
                        }
                    }

                }
            {

            }
            }

        }
        cancelBtn.setOnClickListener { popupBox.visibility = View.GONE  }
        confirmBtn.setOnClickListener { navigateToList }
        return view
    }

    fun processValue(value: String): Unit
    {
        val summary_list: kotlin.collections.List<String> = value.replace("[", "").replace("]", "").replace("\"","").split(",").map{it.trim()}
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




}
