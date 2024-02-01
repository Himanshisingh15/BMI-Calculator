package com.example.bmicalculator

import DbAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bmicalculator.databinding.ActivityHistoryBinding
import com.example.bmicalculator.databinding.ActivityMainBinding
import com.example.bmicalculator.databinding.CustomDialogBinding
import com.example.bmicalculator.databinding.DataSaveCustomDialogBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    lateinit var dbBinding: ActivityHistoryBinding
    private lateinit var dbAdapter: DbAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val unit = listOf<String>("Please Select Unit", "CGS","FPS")

        setContentView(binding.root)

        binding.calcButton.setOnClickListener(this)
        dbAdapter = DbAdapter(this)

        binding.tilFh!!.visibility = View.GONE
        binding.tilPw!!.visibility = View.GONE

        binding.tilHeight!!.visibility = View.GONE
        binding.tilWeight!!.visibility = View.GONE
        binding.calcButton!!.visibility = View.GONE
        binding.result!!.visibility = View.GONE
        binding.viewInfo!!.visibility = View.GONE
        binding.clearButton!!.visibility= View.GONE

        var arrayAdapter =
            ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, unit)
        binding.spinnerUnit!!.adapter = arrayAdapter
        binding.spinnerUnit!!.onItemSelectedListener = this


    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.calcButton -> {
                var height = binding.heightInput.text.toString()
                var weight = binding.weightInput.text.toString()

                if (height.isEmpty()) {
                    binding.heightInput.error = "This field is required"
                }
                if (weight.isEmpty()) {
                    binding.weightInput.error = "This field is required"
                }
                if (height.isNotEmpty() && weight.isNotEmpty()) {
                    calculation()
                }
            }
        }
    }

    fun calculation() {
        var inputHeight = binding.heightInput.text.toString().toFloat()
        var inputWeight = binding.weightInput.text.toString().toFloat()

        var totalHeight = inputHeight * inputHeight

        var bmiValue = (inputWeight / totalHeight) * 10000

        var bmi = (bmiValue * 10.0).roundToInt() / 10.0

        binding.viewInfo.text = "Your BMI value is $bmi"
        binding.viewInfo.visibility = View.VISIBLE

        if (bmi <= 18.5) {
            binding.result!!.text = "You are Underweight"
            binding.result!!.visibility = View.VISIBLE
        } else if (bmi > 18.5 && bmi <= 24.9) {
            binding.result!!.text = "You are Normal"
            binding.result!!.visibility = View.VISIBLE
        } else if (bmi > 24.5 && bmi < 29.9) {
            binding.result!!.text = "You are OverWeight"
            binding.result!!.visibility = View.VISIBLE
        } else {
            binding.result!!.text = "Obese"
            binding.result!!.visibility = View.VISIBLE
        }
        binding.clearButton!!.visibility = View.VISIBLE
        binding.calcButton.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        Log.d("life_cycle", "onStart()")

    }

    override fun onResume() {
        super.onResume()
        dbAdapter.fetchData()
//        dbBinding.listView.adapter = HistoryActivity
    }

    override fun onPause() {
        super.onPause()
        Log.d("life_cycle", "onPause()")

    }

    override fun onStop() {
        super.onStop()
        Log.d("life_cycle", "onStop()")

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("life_cycle", "onRestart()")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("life_cycle", "onDestroy()")

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.item_app -> {
//                var intent = Intent(this@MainActivity, AboutBmiActivity::class.java)
//                startActivity(intent)

                var intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.calculator.net/bmi-calculator.html")
                )
                startActivity(intent)
            }

            R.id.item_chart -> {
                var intentBmiChart = Intent(this@MainActivity, BmiChartActivity::class.java)
                startActivity(intentBmiChart)
            }


            R.id.item_about_developer -> {
                var intentAboutDeveloper =
                    Intent(this@MainActivity, AboutDeveloperActivity::class.java)
                startActivity(intentAboutDeveloper)
            }

            R.id.item_contact -> {
//                var intentContact = Intent(this@MainActivity, SecondActivity::class.java)
//                startActivity(intentContact)

                var custombinding = CustomDialogBinding.inflate(layoutInflater)

                var dialog = Dialog(this@MainActivity)
                dialog.setContentView(custombinding.root)


                dialog.setCancelable(false)

                var layoutManager = WindowManager.LayoutParams()
                layoutManager.width = WindowManager.LayoutParams.MATCH_PARENT
                layoutManager.height = WindowManager.LayoutParams.WRAP_CONTENT
                dialog.window!!.attributes = layoutManager
                dialog.show()
                custombinding.dialBtn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View?) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9319501810"))
                        startActivity(intent)

                        dialog.dismiss()
                    }
                })
                custombinding.callBtn.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(view: View?) {
                        if (ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                Manifest.permission.CALL_PHONE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            var intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:8744828281"))
                            startActivity(intent)

                        } else {
                            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(), 1001)
                        }
                        dialog.dismiss()
                    }
                })
                custombinding.iconCancel.setOnClickListener{
                    dialog.dismiss()
                }

            }

            R.id.item_history -> {
                Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
//
//                DbAdapter.createData(binding.heightInput.editableText?.toString(),binding.weightInput.editableText?.toString())
////                    binding..editText?.text.toString(),binding.mNumber.editText?.text.toString())
//
//                var intentHistory = Intent(this@MainActivity, HistoryActivity::class.java)
//                intentHistory.putExtra("height",binding.heightInput.text.toString())
//                intentHistory.putExtra("weight",binding.weightInput.text.toString())
////                intentHistory.putExtra("bmiValue",binding..text.toString())
////                intentHistory.putExtra("bmiInfo",binding.mobileNum.text.toString())
//
//                startActivity(intentHistory)

            }

            R.id.item_exit -> {
                var builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setTitle("Exit")
                builder.setMessage("Do you want to exit")
                builder.setPositiveButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        finish()
                    }
                })
                builder.setNegativeButton("NO", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        Toast.makeText(this@MainActivity, "Don't Exit", Toast.LENGTH_SHORT).show()

                    }
                })
                builder.show()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, p3: Long) {
        if (adapter!!.getItemAtPosition(position)== "CGS"){
                binding.tilFh!!.visibility = View.GONE
                binding.tilPw!!.visibility = View.GONE
                binding.tilHeight!!.visibility = View.VISIBLE
                binding.tilWeight!!.visibility = View.VISIBLE
                binding.calcButton.visibility = View.VISIBLE
                binding.clearButton!!.visibility=View.GONE

            binding.calcButton.setOnClickListener{
                validation()
            }
            binding.result!!.visibility = View.INVISIBLE
            binding.viewInfo.visibility = View.INVISIBLE

            binding.clearButton!!.setOnClickListener{
                saveData()
                clearData()
                binding.calcButton.visibility = View.VISIBLE
                binding.clearButton!!.visibility = View.INVISIBLE
                }

            }else if (adapter!!.getItemAtPosition(position) == "FPS"){
                binding.tilFh!!.visibility = View.VISIBLE
                binding.tilPw!!.visibility = View.VISIBLE
                binding.tilHeight!!.visibility = View.GONE
                binding.tilWeight!!.visibility = View.GONE
                binding.calcButton.visibility = View.VISIBLE

            binding.calcButton.setOnClickListener{
                fpvalidation()
            }
                binding.result!!.visibility = View.INVISIBLE
                binding.viewInfo.visibility = View.INVISIBLE

            binding.clearButton!!.setOnClickListener{
                saveData()
                clearData()
                binding.calcButton.visibility = View.VISIBLE
                binding.clearButton!!.visibility = View.INVISIBLE
            }
            }
        }
    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this@MainActivity, "Nothing Selected", Toast.LENGTH_SHORT).show()
    }

    fun validation() {
        binding.calcButton.setOnClickListener {
        var height = binding.heightInput.text.toString()
        var weight = binding.weightInput.text.toString()

        if (height.isEmpty()) {
            binding.heightInput.error = "This field is required"
        }
        if (weight.isEmpty()) {
            binding.weightInput.error = "This field is required"
        }
        if (height.isNotEmpty() && weight.isNotEmpty()) {
            calculation()
        }
    }
    }

    fun fpvalidation() {
        binding.calcButton.setOnClickListener {
            var fheight = binding.feetHeight!!.text.toString()
            var pweight = binding.poundWeight!!.text.toString()

            if (fheight.isEmpty()) {
                binding.feetHeight!!.error = "This field is required"
            }
            if (pweight.isEmpty()) {
                binding.poundWeight!!.error = "This field is required"
            }
            if (fheight.isNotEmpty() && pweight.isNotEmpty()) {
                fpCalculation()
            }
        }
    }

    fun clearData(){
        binding.heightInput.text!!.clear()
        binding.weightInput.text!!.clear()
        binding.feetHeight!!.text!!.clear()
        binding.poundWeight!!.text!!.clear()
        binding.viewInfo.text = ""
        binding.result!!.text = ""

    }
    fun fpCalculation(){
        var fHeight = binding.feetHeight!!.text.toString().toFloat()
        var pWeight = binding.poundWeight!!.text.toString().toFloat()
        var  inchHeight = fHeight*12
        var totalHeight  =  inchHeight* inchHeight

        var bmi = (pWeight /totalHeight)*703

        binding.viewInfo.text = "Your BMI value is $bmi"
        binding.viewInfo.visibility = View.VISIBLE

        if (bmi <= 18.5) {
            binding.result!!.text = "You are Underweight"
            binding.result!!.visibility = View.VISIBLE
        } else if (bmi > 18.5 && bmi <= 24.9) {
            binding.result!!.text = "You are Normal"
            binding.result!!.visibility = View.VISIBLE
        } else if (bmi > 24.5 && bmi < 29.9) {
            binding.result!!.text = "You are OverWeight"
            binding.result!!.visibility = View.VISIBLE
        } else {
            binding.result!!.text = "Obese"
            binding.result!!.visibility = View.VISIBLE
        }
        binding.clearButton!!.visibility= View.VISIBLE
        binding.calcButton.visibility = View.INVISIBLE
    }
    fun saveData(){
        binding.clearButton!!.setOnClickListener{
            var saveDatabinding = DataSaveCustomDialogBinding.inflate(layoutInflater)

            var dialog = Dialog(this@MainActivity)
            dialog.setContentView(saveDatabinding.root)


            dialog.setCancelable(false)

            var layoutManager = WindowManager.LayoutParams()
            layoutManager.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutManager.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutManager
            dialog.show()
            saveDatabinding.saveBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    Toast.makeText(this@MainActivity, "Saved Data", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    clearData()
                }
            })
            saveDatabinding.notSaveBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    Toast.makeText(this@MainActivity, "Don't save data", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    clearData()
                }
            })
            saveDatabinding.iconCancel.setOnClickListener{
                dialog.dismiss()
            }
        }
    }
}