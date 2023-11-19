package com.asadullo.retrofit

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.asadullo.retrofit.databinding.ActivityMainBinding
import com.asadullo.retrofit.databinding.DialogItemBinding
import com.asadullo.retrofit.models.MyData
import com.asadullo.retrofit.models.post
import com.asadullo.retrofit.retrofit.Client
import com.asadullo.retrofit.retrofit.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), click {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        load()

        binding.add.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val item = DialogItemBinding.inflate(layoutInflater)


            item.btn.setOnClickListener {
                val post = post(item.holat.text.toString(), item.text.text.toString(), item.lastDate.text.toString(), item.title.text.toString())
                Client.getService()
                    .add(post)
                    .enqueue(object : Callback<MyData> {
                        override fun onResponse(call: Call<MyData>, response: Response<MyData>) {
                            Toast.makeText(this@MainActivity, "Save", Toast.LENGTH_SHORT).show()
                            dialog.cancel()
                            load()
                        }

                        override fun onFailure(call: Call<MyData>, t: Throwable) {

                        }
                    })
            }
            dialog.setView(item.root)



            dialog.show()
        }
    }

    fun load(){
        binding.prog.visibility = View.VISIBLE
        val get = Client.getService().get()
        get.enqueue(object : Callback<List<MyData>>{
            override fun onResponse(call: Call<List<MyData>>, response: Response<List<MyData>>) {
                val list = response.body()
                binding.rv.adapter = Adapter(this@MainActivity, list as ArrayList<MyData>)
                binding.prog.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<MyData>>, t: Throwable) {
                TODO("Not yet implemented")
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
        val dialog = AlertDialog.Builder(this).create()
        val item = DialogItemBinding.inflate(layoutInflater)
        dialog.setView(item.root)
        item.title.setText(user.sarlavha)
        item.text.setText(user.matn)
        item.holat.setText(user.holat)
        item.lastDate.setText(user.oxirgi_muddat)
        item.btn.setOnClickListener {
            val post = post(item.holat.text.toString(), item.text.text.toString(), item.lastDate.text.toString(), item.title.text.toString())
            Client.getService().uptade(user.id, post)
                .enqueue(object : Callback<MyData>{
                    override fun onResponse(call: Call<MyData>, response: Response<MyData>) {
                        Toast.makeText(this@MainActivity, "Edit", Toast.LENGTH_SHORT).show()
                        load()
                    }

                    override fun onFailure(call: Call<MyData>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
        }
        dialog.show()
    }
}