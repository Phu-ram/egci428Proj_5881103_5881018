package com.example.project_personaldoctor.ui.diagnose

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_personaldoctor.Adapter.FeedViewHolder
import com.example.project_personaldoctor.Model.Medicines
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.fragment_diagnose.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

class DiagnoseFragment : Fragment() {
    private val drugsURL = "https://egci428-json-inclass-pae.firebaseio.com/Medicines.json"
    private lateinit var diagnoseViewModel: DiagnoseViewModel

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        diagnoseViewModel =
            ViewModelProviders.of(this).get(DiagnoseViewModel::class.java)
        val root = inflater.inflate(com.example.project_personaldoctor.R.layout.fragment_diagnose, container, false)
        val textView: TextView = root.findViewById(com.example.project_personaldoctor.R.id.text_diagnose)
        //attach recycle view to the fragment
        val recycleView: RecyclerView = root.findViewById(com.example.project_personaldoctor.R.id.drugsList)
        diagnoseViewModel
        diagnoseViewModel.text.observe(this, Observer {
            textView.text = it
        })

         loadRSS()
         val linearLayoutManager = LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false)
         recycleView.layoutManager = linearLayoutManager


        return root
    }

    private fun loadRSS(){
        val client = OkHttpClient()
        val loadRSSAsyncTask = object: AsyncTask<String, String, String>(){
            override fun onPreExecute() {
                super.onPreExecute()
                Toast.makeText(getActivity()!!.getApplicationContext(),"Please wait...", Toast.LENGTH_SHORT).show()
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                var drugsObject: List<Medicines>
                drugsObject = Gson().fromJson<List<Medicines>>(result,object :TypeToken<List<Medicines>>() {}.type)
                val adapter = FeedViewHolder.FeedAdapter(drugsObject,getActivity()!!.getApplicationContext())
                drugsList.adapter = adapter
                adapter!!.notifyDataSetChanged()
                Toast.makeText(getActivity()!!.getApplicationContext(),"Data successfully loaded", Toast.LENGTH_SHORT).show()
            }

            override fun doInBackground(vararg p0: String): String {
                val builder = Request.Builder()
                builder.url(p0[0])
                val request = builder.build()
                try{
                    val response = client.newCall(request).execute()
                    return response.body!!.string()
                }catch (e: Exception){
                    e.printStackTrace()
                }
                return "Data is not found"

            }
        }
        loadRSSAsyncTask.execute(drugsURL.toString())
    }

}