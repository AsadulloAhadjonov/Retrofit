package com.asadullo.retrofit

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asadullo.retrofit.databinding.ActivityMainBinding
import com.asadullo.retrofit.models.MyData
import com.asadullo.retrofit.models.post
import com.asadullo.retrofit.retrofit.Client
import com.asadullo.retrofit.retrofit.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), click {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var list:List<MyData>
    private lateinit var adapter: Adapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val swipeRefreshLayout:SwipeRefreshLayout = binding.referesh
        swipeRefreshLayout.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
            load()
        }

        val currentDate = Calendar.getInstance().time

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        val monthName = monthFormat.format(currentDate)

        val dayOfWeekName = when (dayOfWeek) {
            Calendar.SUNDAY -> "Yakshanba"
            Calendar.MONDAY -> "Dushanba"
            Calendar.TUESDAY -> "Seshanba"
            Calendar.WEDNESDAY -> "Chorshanba"
            Calendar.THURSDAY -> "Payshanba"
            Calendar.FRIDAY -> "Juma"
            Calendar.SATURDAY -> "Shanba"
            else -> ""
        }
        val resultText = "$dayOfWeekName, ${calendar.get(Calendar.DAY_OF_MONTH)} - $monthName"
        binding.date.text = resultText
        load()

        binding.add.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }

    fun load(){
        binding.prog.visibility = View.VISIBLE
        val get = Client.getService().get()
        get.enqueue(object : Callback<List<MyData>>{
            override fun onResponse(call: Call<List<MyData>>, response: Response<List<MyData>>) {
                list = response.body()!!
                adapter = Adapter(this@MainActivity, list as ArrayList<MyData>)
                binding.rv.adapter = adapter
                binding.prog.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<MyData>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Internet bilan aloqa yo'q", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun click(user: MyData) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Ogohlantirish")
        dialog.setMessage("${user.sarlavha}ni o'chirmoqchimisiz")
        dialog.setPositiveButton("ha", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                Client.getService().delete(user.id)
                    .enqueue(object :Callback<Int>{
                        override fun onResponse(call: Call<Int>, response: Response<Int>) {
                            Toast.makeText(this@MainActivity, "O'chirildi", Toast.LENGTH_SHORT).show()
                            load()
                        }

                        override fun onFailure(call: Call<Int>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        })
        dialog.show()
    }

    override fun uptade(user: MyData) {
        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("MyData", user)
        intent.putExtra("pos", 1)
        startActivity(intent)
    }
}