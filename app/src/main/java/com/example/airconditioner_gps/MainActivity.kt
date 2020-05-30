package com.example.airconditioner_gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var desiredtempdb: DatabaseReference
    lateinit var aircondb: DatabaseReference
    lateinit var tempdb: DatabaseReference
    var bestTime = 20
    var timeToHome = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        getCurrentTemp()
    }


    private fun init(){
        desiredtempdb = FirebaseDatabase.getInstance().getReference("DesiredTempDB/Place")
        aircondb = FirebaseDatabase.getInstance().getReference("AirconDB/Place")

        tempsetbtn.setOnClickListener {
            if(desiredTempEdit.text.toString() != "") {
                val homeInfo = HomeInfo(Integer.parseInt(desiredTempEdit.text.toString()))
                desiredtempdb.child("home").setValue(homeInfo)
            }
        }

        onbtn.setOnClickListener {
            lateinit var airconInfo: AirconInfo
            if(timeToHome > bestTime){
                airconInfo = AirconInfo("OFF", "강")
            }else{
                airconInfo = AirconInfo("ON", "강")
            }
            aircondb.child("home").setValue(airconInfo)
        }

        offbtn.setOnClickListener {
            var airconInfo = AirconInfo("OFF", "강")
            aircondb.child("home").setValue(airconInfo)
        }
    }

    fun getCurrentTemp(){
        tempdb = FirebaseDatabase.getInstance().getReference("TempDB/Place")
        tempdb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                //database의 값이 갱신되면 자동 호출
                val value = dataSnapshot?.value
                homeTemp.text = "$value"
                homeHumidity.text = "$value"
            }

            override fun onCancelled(p0: DatabaseError) {
                println("Failed to read value.")
            }
        })
    }
}
