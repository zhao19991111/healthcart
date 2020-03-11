package com.example.healthcart


import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.Navigation
import java.util.ArrayList
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.ScrollView
import androidx.navigation.fragment.findNavController


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
        val navigateToSelect = Navigation.createNavigateOnClickListener(R.id.action_list_to_select)
        finishBtn.setOnClickListener{
            run{
                concatStrArrayToDB("new_food_list", "food_list",1)
                concatStrArrayToDB("new_data_list", "data_list",2)
                findNavController().navigate(ListDirections.actionListToHome())
            }
        }
        reselectBtn.setOnClickListener(navigateToSelect)
        val args = ListArgs.fromBundle(arguments!!)
        val food_list = stringToArray(args.foodStr)
        val data_list = stringToArray2(args.dataStr)
        val button_list = arrayOf(R.id.expand_btn0, R.id.expand_btn1, R.id.expand_btn2, R.id.expand_btn3, R.id.expand_btn4, R.id.expand_btn5, R.id.expand_btn6, R.id.expand_btn7, R.id.expand_btn8)
        val row_list = arrayOf(R.id.table_row0, R.id.table_row1, R.id.table_row2, R.id.table_row3, R.id.table_row4, R.id.table_row5, R.id.table_row6, R.id.table_row7, R.id.table_row8)
        val calorie_entry_list = arrayOf(R.id.item_cal0, R.id.item_cal1, R.id.item_cal2, R.id.item_cal3, R.id.item_cal4, R.id.item_cal5, R.id.item_cal6, R.id.item_cal7, R.id.item_cal8)
        val food_entry_list = arrayOf(R.id.item_name0, R.id.item_name1, R.id.item_name2, R.id.item_name3, R.id.item_name4, R.id.item_name5, R.id.item_name6, R.id.item_name7, R.id.item_name8)
        var total_cal = 0
        view.findViewById<ScrollView>(R.id.detail_list).visibility = View.INVISIBLE

        button_list.forEach { ele ->
            view.findViewById<Button>(ele).visibility = View.INVISIBLE
        }

        food_list.forEachIndexed{
            ind, ele -> run {
                view.findViewById<Button>(button_list[ind]).visibility = View.VISIBLE
                view.findViewById<TextView>(food_entry_list[ind]).text = food_list[ind]
            }
        }

        data_list.forEachIndexed{
            ind, ele -> run {
                view.findViewById<TextView>(calorie_entry_list[ind]).text = data_list[ind][0]
                total_cal += data_list[ind][0].toInt()
            }
        }

        view.findViewById<Button>(R.id.return_list_btn).setOnClickListener{
            view.findViewById<ScrollView>(R.id.detail_list).visibility = View.INVISIBLE
            view.findViewById<ScrollView>(R.id.summary_list).visibility = View.VISIBLE
        }

        button_list.forEachIndexed{
            ind, ele -> run{
                view.findViewById<Button>(ele).setOnClickListener{
                    view.findViewById<ScrollView>(R.id.detail_list).visibility = View.VISIBLE
                    view.findViewById<ScrollView>(R.id.summary_list).visibility = View.INVISIBLE
                    view.findViewById<TextView>(R.id.cal_detail_val).text = data_list[ind][0]
                    view.findViewById<TextView>(R.id.fats_detail_val).text = data_list[ind][2]
                    view.findViewById<TextView>(R.id.protein_detail_val).text = data_list[ind][3]
                    view.findViewById<TextView>(R.id.carb_detail_val).text = data_list[ind][1]
                }
            }
        }

        val title_str = "Total Calories are: "
        val calorie_str = total_cal.toString()
        val title = view.findViewById<TextView>(R.id.info_title)
        title.setText(title_str + calorie_str, TextView.BufferType.SPANNABLE )
        val s = title.getText() as Spannable
        val start = title_str.length
        val end = start + calorie_str.length
        s.setSpan(ForegroundColorSpan(-0x10000), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return view
    }

    private fun concatStrArrayToDB(keynew: String, key: String, lv: Int): Unit {
        val strnew = getStrFromDB(keynew)
        val str = getStrFromDB(key)
        if (lv == 1) {
            var arrnew = stringToArray(strnew)
            var arr = stringToArray(str)
            arr.addAll(arrnew)
            saveStrToDB(key, arrayToString(arr))
        }
        else if (lv == 2)
        {
            var arrnew = stringToArray2(strnew)
            var arr = stringToArray2(str)
            arr.addAll(arrnew)
            saveStrToDB(key, array2ToString(arr))
        }

    }


    private fun getStrFromDB(key: String): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return ""
        return sharedPref!!.getString(key,"")!!
    }

    private fun saveStrToDB( key: String, str: String): Unit
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
