package com.example.workspace.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.workspace.R
import com.example.workspace.databinding.ActivityMainBinding
import com.example.workspace.ui.home.HomeFragment
import com.example.workspace.ui.location.LocationFragment

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViews()
        viewBinding.content.homeBottomNavigation.selectedItemId = R.id.Home

       // ApiManager.getApis()
         //   .getSources(apikey)
           // .enqueue(object :Callback<SourcesResponse>{
             //   override fun onResponse(
               //     call: Call<SourcesResponse>,
                 //   response: Response<SourcesResponse>
                //) {
                  //  Log.e("response",response.body().toString())
                //}

                //override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                  //  Log.e("error",t.localizedMessage?:"")
                //}

            //})

    }

    private  fun initViews(){
        viewBinding.content.homeBottomNavigation.setOnItemSelectedListener {item->
            when (item.itemId) {
                R.id.Home -> {
                    showFragment(HomeFragment())


                }
                R.id.location -> {
                     showFragment(LocationFragment())

                }
            }

            return@setOnItemSelectedListener true
        }
    }

private fun showFragment(fragment: Fragment){

    supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragment_container,fragment)
        .commit()
}

}