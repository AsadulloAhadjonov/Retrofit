package com.asadullo.retrofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asadullo.retrofit.databinding.ItemRvBinding
import com.asadullo.retrofit.models.MyData

class Adapter(var click: click, var list: ArrayList<MyData>) : RecyclerView.Adapter<Adapter.Vh>() {
    inner class Vh(var item: ItemRvBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: MyData) {
            item.name.text = user.sarlavha
            item.number.text = user.matn
            item.delete.setOnClickListener {
                click.click(user)
            }

            item.root.setOnClickListener {
                click.uptade(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }
}

interface click{
    fun click(user: MyData)
    fun uptade(user: MyData)
}