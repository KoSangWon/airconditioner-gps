package com.example.airconditioner_gps

import java.io.Serializable

//이름 현재온도 희망온도 순
class MyData(var name:String, var currentTemp:Float, var desiredTemp:Float) :Serializable{
}