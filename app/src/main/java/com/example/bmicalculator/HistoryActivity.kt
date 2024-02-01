package com.example.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.bmicalculator.databinding.CustomListViewBinding

class HistoryActivity(var list: List<UserData>): BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        var myView = LayoutInflater.from(viewGroup!!.context).inflate(R.layout.activity_history, viewGroup, false)
        var dbBinding = CustomListViewBinding.bind(myView)
        var person = list[position]
        dbBinding.tvHeight.text= "Name - ${person.height}"
        dbBinding.tvWeight.text = "Mobile - ${person.weight}"
        dbBinding.tvBmi.text = "Mobile - ${person.bmiValue} ${person.bmiInfo}"

        return myView
    }
}