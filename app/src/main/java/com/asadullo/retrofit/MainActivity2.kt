package com.asadullo.retrofit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import com.asadullo.retrofit.databinding.ActivityMain2Binding
import com.asadullo.retrofit.models.MyData
import com.asadullo.retrofit.models.post
import com.asadullo.retrofit.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity2 : AppCompatActivity() {
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    private lateinit var text:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val pos = intent.getIntExtra("pos", 0)

        binding.back.setOnClickListener {
            finish()
        }

        binding.false1.setOnClickListener {
            binding.dateName.text = "00-00-0000"
        }

        if (pos == 1){
            edit()
        }else{
            add()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun uptadeTime(myCalendar:Calendar){
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        binding.dateName.text = sdf.format(myCalendar.time)
    }

    private fun add(){
        text = ""

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.holat.adapter = adapter
        binding.holat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                text = parent?.getItemAtPosition(position).toString()
                binding.holat.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Boshqicha ishni qilish
            }
        }

        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            calendar.set(Calendar.YEAR, i)
            calendar.set(Calendar.MONTH, i2)
            calendar.set(Calendar.DAY_OF_MONTH, i3)
            uptadeTime(calendar)
        }

        binding.date.setOnClickListener {
            DatePickerDialog(this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy")

        binding.done.setOnClickListener {
            if (binding.dateName.text.toString() == "00-00-0000"){
                Toast.makeText(this, "Sanani kiriting", Toast.LENGTH_SHORT).show()
            }else{
                val post = post(text, binding.edtDescription.text.toString(), sdf.format(calendar.time), binding.edtName.text.toString())
                Client.getService()
                    .add(post)
                    .enqueue(object : Callback<MyData> {
                        override fun onResponse(call: Call<MyData>, response: Response<MyData>) {
                            Toast.makeText(this@MainActivity2, "Save", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<MyData>, t: Throwable) {
                            Toast.makeText(this@MainActivity2, "Error", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun edit(){

        text = ""

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.holat.adapter = adapter
        binding.holat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                text = parent?.getItemAtPosition(position).toString()
                binding.holat.setSelection(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Boshqicha ishni qilish
            }
        }

        val user = intent.getSerializableExtra("MyData") as MyData
        binding.edtName.setText(user.sarlavha)
        binding.dateName.setText(user.oxirgi_muddat)
        when(user.holat){
            "Kiritilmoqda"->{
                binding.holat.setSelection(0)}
            "Bajarilmoqda"->{
                binding.holat.setSelection(1)}
            "Bajarildi"->{
                binding.holat.setSelection(2)}
            else->{
                binding.holat.setSelection(0)
            }
        }
        binding.edtDescription.setText(user.matn)

        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            calendar.set(Calendar.YEAR, i)
            calendar.set(Calendar.MONTH, i2)
            calendar.set(Calendar.DAY_OF_MONTH, i3)
            uptadeTime(calendar)
        }

        binding.date.setOnClickListener {
            DatePickerDialog(this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy")

        binding.done.setOnClickListener {
            if (binding.dateName.text.toString() == "00-00-0000"){
                Toast.makeText(this, "Sanani kiriting", Toast.LENGTH_SHORT).show()
            }else {
                val post = post(
                    text,
                    binding.edtDescription.text.toString(),
                    sdf.format(calendar.time),
                    binding.edtName.text.toString()
                )
                Client.getService().uptade(user.id, post)
                    .enqueue(object : Callback<MyData> {
                        override fun onResponse(call: Call<MyData>, response: Response<MyData>) {
                            Toast.makeText(this@MainActivity2, "Edit", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<MyData>, t: Throwable) {
                            Toast.makeText(this@MainActivity2, "Error", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

    }
}