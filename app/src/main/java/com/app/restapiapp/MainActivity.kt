package com.app.restapiapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.restapiapp.adapters.UserAdapter
import com.app.restapiapp.databinding.ActivityMainBinding
import com.app.restapiapp.models.User
import com.app.restapiapp.models.Users
import com.app.restapiapp.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val usersList = mutableListOf<User>()
    private lateinit var userAdapter: UserAdapter
    private var pageNumber = 1
    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter(this, usersList)
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        loadUsers(pageNumber)

        // Add scroll listener for pagination
        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        pageNumber++
                        loadUsers(pageNumber)
                    }
                }
            }
        })
    }

    private fun loadUsers(page: Int) {
        isLoading = true
        binding.progressCircular.visibility = View.VISIBLE
        val apiService = RetrofitClient.getRetrofitInstance(this)
        apiService.getUsers(page).enqueue(object : Callback<Users> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                isLoading = false
                binding.progressCircular.visibility = View.GONE
                val users = response.body()
                if (users != null) {
                    if (users.data.isNotEmpty()) {
                        usersList.addAll(users.data)
                        userAdapter.notifyDataSetChanged()
                    } else {
                        isLastPage = true
                        Toast.makeText(
                            this@MainActivity,
                            "No more data to load",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                isLoading = false
                binding.progressCircular.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}