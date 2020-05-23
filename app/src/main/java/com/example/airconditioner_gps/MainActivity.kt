package com.example.airconditioner_gps

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.jsoup.Jsoup
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runOnUiThread{
            startJSONTask()
        }
    }

    fun startJSONTask(){
        val task = MyAsyncTask(this)
        task.execute(URL("http://54.180.80.25/temp.php"))
    }

    class MyAsyncTask(context: MainActivity): AsyncTask<URL, Unit, Unit>(){
        val activityreference = WeakReference(context)

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: URL?): Unit {
            var activity = activityreference.get()

            val doc = Jsoup.connect(params[0].toString()).ignoreContentType(true).get()
            val json = JSONObject(doc.text())
            val array = json.getJSONArray("result")
            val temperature = array.getJSONObject(0).getString("temperature")
            try {
                activity?.tempText?.text = temperature
            }catch (e:Exception){
                return
            }

            Log.i("Temperature ", temperature)
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            val activity = activityreference.get()
            if(activity==null || activity.isFinishing) {
                return
            }
        }
    }
}