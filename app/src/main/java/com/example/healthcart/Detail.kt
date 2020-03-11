package com.example.healthcart


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.navigation.Navigation
import java.lang.Math.round
import java.util.ArrayList
import android.content.Intent
import android.content.Intent.getIntent
import androidx.core.app.ActivityCompat.recreate
import kotlinx.android.synthetic.main.fragment_list.*


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
        val food_list = stringToArray(getStrFromDB("food_list"))
        val data_list = stringToArray2(getStrFromDB("data_list"))
        val table = view.findViewById<TableLayout>(R.id.detail_table)
        val returnBtn = view.findViewById<Button>(R.id.home_btn)
        val clearBtn = view.findViewById<Button>(R.id.clear_btn)
        val navigateToHome = Navigation.createNavigateOnClickListener(R.id.action_detail_to_home)
        returnBtn.setOnClickListener(navigateToHome)
        clearBtn.setOnClickListener{
            saveStrToDB("food_list", "")
            saveStrToDB("data_list", "")
            activity!!.recreate()
        }
        var sum_cal = 0
        var sum_protein = 0f
        var sum_fats = 0f
        var sum_carbs = 0f
        data_list.forEach{
            ele -> run {
            sum_cal += ele[0].toInt()
            sum_protein += ele[3].replace("g","").trim().toFloat()
            sum_fats += ele[2].replace("g","").trim().toFloat()
            sum_carbs += ele[1].replace("g","").trim().toFloat()
            }
        }
        if (food_list.size == 0)
        {
            val tv = TextView(this.context)
            tv.textSize = 20f
            tv.setBackgroundColor(Color.GREEN)
            tv.gravity = Gravity.CENTER
            tv.text = "Let's add some food to the food list!"
            table.addView(tv)
        }
        else
        {
            food_list.forEachIndexed { ind, ele ->
            run {
                var tb_row1 = TableRow(this.context)
                var food_item = TextView(this.context)
                var calories = TextView(this.context)
                tb_row1.setBackgroundColor(Color.GRAY)
                food_item.setTextColor(Color.WHITE)
                calories.setTextColor(Color.WHITE)
                tb_row1.id = View.generateViewId()
                food_item.id = View.generateViewId()
                calories.id = View.generateViewId()
                food_item.text = ele
                food_item.textSize = 18f
                food_item.gravity = Gravity.CENTER
                calories.text = data_list[ind][0]
                calories.gravity = Gravity.CENTER
                calories.textSize = 18f
                tb_row1.addView(food_item)
                tb_row1.addView(calories)
                var tb_row2= TableRow(this.context)
                var proteins = TextView(this.context)
                var fats = TextView(this.context)
                var carbs = TextView(this.context)
                tb_row2.setBackgroundColor(Color.LTGRAY)
                proteins.setTextColor(Color.BLACK)
                fats.setTextColor(Color.BLACK)
                carbs.setTextColor(Color.BLACK)
                proteins.id = View.generateViewId()
                fats.id = View.generateViewId()
                carbs.id = View.generateViewId()
                proteins.text = data_list[ind][3]
                proteins.gravity = Gravity.CENTER
                proteins.textSize = 14f
                fats.text = data_list[ind][2]
                fats.gravity = Gravity.CENTER
                fats.textSize = 14f
                carbs.text = data_list[ind][1]
                carbs.gravity = Gravity.CENTER
                carbs.textSize = 14f
                tb_row2.addView(carbs)
                tb_row2.addView(fats)
                tb_row2.addView(proteins)
                tb_row1.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
                )
                tb_row2.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                table.addView(
                    tb_row1,
                    TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                table.addView(
                    tb_row2,
                    TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                )
             }
            }
            var tb_row1 = TableRow(this.context)
            var food_item = TextView(this.context)
            var calories = TextView(this.context)
            tb_row1.setBackgroundColor(Color.GRAY)
            food_item.setTextColor(Color.RED)
            calories.setTextColor(Color.RED)
            tb_row1.id = View.generateViewId()
            food_item.id = View.generateViewId()
            calories.id = View.generateViewId()
            food_item.text = "Total"
            food_item.textSize = 20f
            food_item.gravity = Gravity.CENTER
            calories.text = sum_cal.toString()
            calories.gravity = Gravity.CENTER
            calories.textSize = 20f
            tb_row1.addView(food_item)
            tb_row1.addView(calories)
            var tb_row2= TableRow(this.context)
            var proteins = TextView(this.context)
            var fats = TextView(this.context)
            var carbs = TextView(this.context)
            tb_row2.setBackgroundColor(Color.LTGRAY)
            proteins.setTextColor(Color.RED)
            fats.setTextColor(Color.RED)
            carbs.setTextColor(Color.RED)
            proteins.id = View.generateViewId()
            fats.id = View.generateViewId()
            carbs.id = View.generateViewId()
            proteins.text = String.format("%.1f g", sum_protein)
            proteins.gravity = Gravity.CENTER
            proteins.textSize = 16f
            fats.text = String.format("%.1f g", sum_fats)
            fats.gravity = Gravity.CENTER
            fats.textSize = 16f
            carbs.text = String.format("%.1f g", sum_carbs)
            carbs.gravity = Gravity.CENTER
            carbs.textSize = 16f
            tb_row2.addView(carbs)
            tb_row2.addView(fats)
            tb_row2.addView(proteins)
            tb_row1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tb_row2.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            table.addView(
                tb_row1,
                TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            )
            table.addView(
                tb_row2,
                TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
        return view
    }

    private fun getStrFromDB(key: String): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return ""
        return sharedPref!!.getString(key,"NONE")!!
    }

    private fun saveStrToDB( key: String, str: String): Unit
    {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, str)
            commit()
        }
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
