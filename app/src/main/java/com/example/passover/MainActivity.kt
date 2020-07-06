package com.example.passover

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.passover.rest.RestResult
import com.example.passover.rest.RestRetrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    //퍼미션 응답 처리 코드
    private val multiplePermissionsCode = 100

    //필요한 퍼미션 리스트
    //원하는 퍼미션을 이곳에 추가하면 된다.
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //휴대폰 정보를 가져오기한 권한 체크
        checkPermissions()

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
    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_LONG).show()
            } else {
                //Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                var phoneNum : String = "";
                val telManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if(telManager.line1Number != null) {
                    phoneNum = telManager.line1Number.toString()
                    Log.d("debug", "phoneNum:" + phoneNum)
                    //Toast.makeText(applicationContext, "전화번호:"+phoneNum, Toast.LENGTH_LONG).show()
                }else{
                    //Toast.makeText(applicationContext, "전화번호 획득 실패", Toast.LENGTH_LONG).show()
                    phoneNum = "010-0000-1111";
                }
                if(phoneNum != null && phoneNum != ""){
                    val qrStr : String  = result.contents;
                    RestRetrofit.getService().saveData(data = qrStr, phone= phoneNum).enqueue( object :
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
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //퍼미션 체크 및 권한 요청 함수
    private fun checkPermissions() {
        //거절되었거나 아직 수락하지 않은 권한(퍼미션)을 저장할 문자열 배열 리스트
        var rejectedPermissionList = ArrayList<String>()

        //필요한 퍼미션들을 하나씩 끄집어내서 현재 권한을 받았는지 체크
        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //만약 권한이 없다면 rejectedPermissionList에 추가
                rejectedPermissionList.add(permission)
            }else{
                Log.d("TAG", "The user has accept to " + permission.toString())
            }
        }
        //거절된 퍼미션이 있다면...
        if(rejectedPermissionList.isNotEmpty()){
            //권한 요청!
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(this, rejectedPermissionList.toArray(array), multiplePermissionsCode)
        }
    }

    //권한 요청 결과 함수
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            multiplePermissionsCode -> {
                if(grantResults.isNotEmpty()) {
                    for((i, permission) in permissions.withIndex()) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //권한 획득 실패
                            Log.d("TAG", "The user has denied to $permission")
                            Log.d("TAG", "I can't work for you anymore then. ByeBye!")
                            Toast.makeText(applicationContext, "앱을 이용하기위한 휴대폰정보 열람권한이 필요합니다.", Toast.LENGTH_LONG).show()
                        }else{
                            Log.d("TAG", "The user has accept to $permission")
                        }
                    }   //for end.
                }   //if end.
            }
        }   //when end.
    }

}