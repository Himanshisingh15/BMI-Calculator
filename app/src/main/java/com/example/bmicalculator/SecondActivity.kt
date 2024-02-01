package com.example.bmicalculator

import android.content.Context
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class SecondActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var dialbtn: MaterialButton
    lateinit var callbtn: MaterialButton

//    var context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        dialbtn=findViewById(R.id.dial_btn)
        callbtn=findViewById(R.id.call_btn)

        dialbtn.setOnClickListener(this)
        callbtn.setOnClickListener(this)

        }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.dial_btn ->{
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9319501810"))
                startActivity(intent)
            }

            R.id.call_btn ->{
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                {
                    var intent = Intent(Intent.ACTION_CALL,Uri.parse("tel:8744828281"))
                    startActivity(intent)

                }
                else{
                    ActivityCompat.requestPermissions(this@SecondActivity, arrayOf(Manifest.permission.CALL_PHONE),1001)
                }

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1001 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            var intent = Intent(Intent.ACTION_CALL,Uri.parse("tel:8744828281"))
            startActivity(intent)

//            Toast.makeText(this,"Permission Granted ", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            Toast.makeText(this,"Permission Denied ", Toast.LENGTH_SHORT).show()
        }
    }

}

