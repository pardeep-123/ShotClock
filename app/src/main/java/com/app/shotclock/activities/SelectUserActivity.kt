package com.app.shotclock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.adapters.SelectUserAdapter
import com.app.shotclock.databinding.ActivitySelectUserBinding
import com.app.shotclock.models.RequestListResponseModel

class SelectUserActivity : AppCompatActivity() {

    var listUser = ArrayList<RequestListResponseModel.RequestListResponseBody>()

    lateinit var binding:ActivitySelectUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelectUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listUser = intent.getSerializableExtra("model") as ArrayList<RequestListResponseModel.RequestListResponseBody>

        listUser.forEach {
            it.isSelected=false
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        setAdapter()

        binding.btDone.setOnClickListener {

            var user2Id=""
            var username=""

            for (i in 0 until listUser.size){
                if (listUser[i].isSelected){

                    user2Id=listUser[i].requestTo
                    username=listUser[i].username
                    break
                }
            }
             if (username.isEmpty()){

                 Toast.makeText(this,"Please select a user first",Toast.LENGTH_SHORT).show()
             }else{

                 var intent=Intent(this,PlanListActivity::class.java)

                 intent.putExtra("user2Id",user2Id)
                 intent.putExtra("username",username)
                 startActivity(intent)

             }
        }
    }
    lateinit var  adapter:SelectUserAdapter
    private fun setAdapter() {

        adapter = SelectUserAdapter(this, listUser)
        binding.rvSelectUser.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.rvSelectUser.adapter = adapter
    }

    override fun onBackPressed() {
       /* super.onBackPressed()*/
        val intent = Intent(this@SelectUserActivity, HomeActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}