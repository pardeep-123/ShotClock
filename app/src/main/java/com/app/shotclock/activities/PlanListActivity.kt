package com.app.shotclock.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.adapters.PLanListAdapter
import com.app.shotclock.databinding.ActivityPlanListBinding

class PlanListActivity : AppCompatActivity() {

    var user2Id=""
    var username=""
    lateinit var adapter:PLanListAdapter
    lateinit var binding: ActivityPlanListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlanListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getStringExtra("user2Id")!=null){
            user2Id=  intent.getStringExtra("user2Id")!!
        }

        if (intent.getStringExtra("username")!=null){
            username=intent.getStringExtra("username")!!
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btBuy.setOnClickListener {
            val intent=Intent(this,HomeActivity::class.java)
            intent.putExtra("sender_id",user2Id)
            intent.putExtra("sender_name",username)
            intent.putExtra("notification_code","3")
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        val list=ArrayList<Model>()
        list.add(Model("$5.99","for 1 Day","Connect with your matches for 1 day"))
        list.add(Model("$9.99","for 1 Month","Connect with your matches for 1 Month"))

        adapter= PLanListAdapter(this,list)
        binding.rvPlanList.layoutManager=LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        binding.rvPlanList.adapter=adapter
    }

    data class Model(val price:String ,val title:String ,var description:String ,var isSelected:Boolean=false ){

    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}