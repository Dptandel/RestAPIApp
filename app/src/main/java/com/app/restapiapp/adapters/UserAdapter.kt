package com.app.restapiapp.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.restapiapp.R
import com.app.restapiapp.databinding.DialogUserDetailsBinding
import com.app.restapiapp.databinding.ItemUserBinding
import com.app.restapiapp.models.User
import com.app.restapiapp.retrofit.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAdapter(private val context: Context, private val usersList: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = usersList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = usersList[position]

        holder.binding.apply {
            Picasso.get().load(user.avatar).into(ivUser)
            tvName.text = "${user.first_name} ${user.last_name}"
            tvEmail.text = user.email

            userItem.setOnClickListener {
                val apiService = RetrofitClient.getRetrofitInstance(context)
                apiService.getUser(user.id).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        showUserDetailsDialog(user)
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(context, "Fail to Load Data...!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showUserDetailsDialog(user: User?) {

        val dialog = Dialog(context)
        val dialogBinding =
            DialogUserDetailsBinding.inflate(LayoutInflater.from(context), null, false)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.apply {
            Picasso.get().load(user?.avatar).into(ivUser)
            tvName.text = "${user?.first_name} ${user?.last_name}"
            tvEmail.text = user?.email
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

    }
}