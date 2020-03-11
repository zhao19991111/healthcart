package com.example.healthcart


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import java.util.ArrayList
import androidx.appcompat.widget.LinearLayoutCompat


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
        val weightText = view.findViewById<TextView>(R.id.weight_info)
        val heightText = view.findViewById<TextView>(R.id.height_info)
        val ageText = view.findViewById<TextView>(R.id.age_info)
        val selectBtn = view.findViewById<Button>(R.id.select)
        val navigateToUpdate = Navigation.createNavigateOnClickListener(R.id.action_home_to_update)
        val navigateToSelect = Navigation.createNavigateOnClickListener(R.id.action_home_to_select)
        val navigateToDetail = Navigation.createNavigateOnClickListener(R.id.action_home_to_detail)
        updateBtn.setOnClickListener(navigateToUpdate)
        selectBtn.setOnClickListener(navigateToSelect)
        weightText.setOnClickListener(navigateToUpdate)
        heightText.setOnClickListener(navigateToUpdate)
        ageText.setOnClickListener(navigateToUpdate)
        val food_list = stringToArray(getStrFromDB("food_list"))
        val data_list = stringToArray2(getStrFromDB("data_list"))
        Log.e("food", getStrFromDB("food_list"))
        Log.e("data", getStrFromDB("data_list"))
        val weight_str = getStrFromDB("weight")
        val height_str = getStrFromDB("height")
        val age_str = getStrFromDB ("age")
        Log.e("weight", weight_str)
        if (weight_str != "NONE" && height_str != "NONE" && age_str != "NONE")
        {
            val weight = weight_str.toDouble()
            val height = height_str.toDouble()
            val age = age_str.toInt()
            val height_unit = getStrFromDB("weight_unit")
            val weight_unit = getStrFromDB("height_unit")
            weightText.text = String.format("Weight: %.1f %s" , weight, weight_unit)
            heightText.text = String.format("Height: %.1f %s" , height, height_unit)
            ageText.text = String.format("Age: %d" , age)
        }
        val table_layout = view.findViewById<TableLayout>(R.id._food_list)
        if (food_list.size == 0 || data_list.size == 0)
        {
            val tv = TextView(this.context)
            tv.textSize = 20f
            tv.setBackgroundColor(Color.GREEN)
            tv.gravity = Gravity.CENTER
            tv.text = "Let's add some food to the food list!"
            table_layout.addView(tv)
        }
        else
        {
            food_list.forEachIndexed { index, ele ->
                run{
                    var tb_row = TableRow(this.context)
                    var food_item = TextView(this.context)
                    var calories = TextView(this.context)
                    if (index%2 == 0)
                    {
                        tb_row.setBackgroundColor(Color.GRAY)
                        food_item.setTextColor(Color.WHITE)
                        calories.setTextColor(Color.WHITE)
                    }
                    else {
                        tb_row.setBackgroundColor(Color.LTGRAY)
                        food_item.setTextColor(Color.BLACK)
                        calories.setTextColor(Color.BLACK)
                    }
                    tb_row.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT)
                    tb_row.id = View.generateViewId()
                    food_item.id = View.generateViewId()
                    calories.id = View.generateViewId()
                    food_item.text = ele
                    food_item.textSize = 16f
                    food_item.gravity = Gravity.CENTER
                    calories.text = data_list[index][0]
                    calories.gravity = Gravity.CENTER
                    calories.textSize = 16f
                    tb_row.addView(food_item)
                    tb_row.addView(calories)
                    table_layout.addView(tb_row, TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT))
                }
            }
        }
        var total_cal = 0
        var desired_cal = 3000
        data_list.forEachIndexed{
                ind, ele -> total_cal += data_list[ind][0].toInt()
        }
        val title_str = "Remaining Calories: "
        val calorie_str = (desired_cal - total_cal).toString()
        val desired_cal_str = desired_cal.toString()
        val title = view.findViewById<TextView>(R.id.progress_title)
        title.setText(title_str + calorie_str + "/" + desired_cal_str, TextView.BufferType.SPANNABLE )
        val s = title.getText() as Spannable
        val start1 = title_str.length
        val end1 = start1 + calorie_str.length
        val start2 = end1 + 1
        val end2 = start2 + desired_cal_str.length
        s.setSpan(ForegroundColorSpan(-0x10000), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(StyleSpan(Typeface.BOLD), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(ForegroundColorSpan(-0x10000), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(StyleSpan(Typeface.BOLD), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        title.setOnClickListener(navigateToDetail)
        return view
    }

    private fun getStrFromDB(key: String): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return ""
        return sharedPref!!.getString(key,"NONE")!!
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

    private fun saveStrToDB(key: String, str: String): Unit
    {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, str)
            commit()
        }
    }


}
