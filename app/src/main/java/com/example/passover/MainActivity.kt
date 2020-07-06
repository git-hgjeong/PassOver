package com.example.passover

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.passover.rest.RestResult
import com.example.passover.rest.RestRetrofit
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            IntentIntegrator(this).initiateScan();
        }
/*
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Get the QR-Code scan results:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show()
            } else {
                //Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                val qrStr : String  = result.contents;
                RestRetrofit.getService().saveData(data = qrStr).enqueue( object :
                    Callback<RestResult> {
                    override fun onFailure(call: Call<RestResult>, t: Throwable) {
                        Log.d("debug", "Fail.")
                        Toast.makeText(applicationContext, "Connect Fail", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<RestResult>, response: Response<RestResult>) {
                        Log.d("debug", "Success.")
                        Log.d("debug", response.toString())
                        if (response.isSuccessful) {
                            val ret = response.body()
                            Log.d("debug", ret?.RESULT_MSG)
                            Toast.makeText(applicationContext, "출석 되었습니다.", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext, "출석체크를 실패 하였습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}