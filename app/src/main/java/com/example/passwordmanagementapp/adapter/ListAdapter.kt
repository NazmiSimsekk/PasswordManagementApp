package com.example.passwordmanagementapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanagementapp.R
import com.example.passwordmanagementapp.databinding.PasswordListBinding
import com.example.passwordmanagementapp.model.PassModel
import com.example.passwordmanagementapp.view.ListFragmentDirections

class ListAdapter(private val passList: ArrayList<PassModel>) : RecyclerView.Adapter<ListAdapter.PassViewHolder>() {

    var positionId: String? = null

    class PassViewHolder(val dataBinding: PasswordListBinding) : RecyclerView.ViewHolder(dataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<PasswordListBinding>(layoutInflater, R.layout.password_list, parent, false)
        return PassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PassViewHolder, position: Int) {
        val passModel = passList[position]
        holder.dataBinding.userData = passModel
        holder.dataBinding.executePendingBindings()  // Binding işlemlerini hemen gerçekleştirir

        holder.itemView.setOnClickListener {
            val positionId = passModel.id
            detailClickListener(it, positionId)
        }
    }

    override fun getItemCount(): Int {
        return passList.size
    }

    // Verileri güncellemek için ek bir fonksiyon ekleyebiliriz
    fun updatePassList(newPassList: List<PassModel>) {
        passList.clear()
        passList.addAll(newPassList)
        notifyDataSetChanged()  // Tüm listeyi günceller
    }

    private fun detailClickListener(v: View, positionId: String?) {
        val action = ListFragmentDirections.actionListFragmentToDetailFragment("detailButtonClick", positionId = positionId)
        Navigation.findNavController(v).navigate(action)
        println("Deneme basıldı $positionId")
    }
}
