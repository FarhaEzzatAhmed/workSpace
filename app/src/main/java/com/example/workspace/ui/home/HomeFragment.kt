package com.example.workspace.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.workspace.R
import com.example.workspace.api.model.ApiConstant
import com.example.workspace.api.model.ApiManager
import com.example.workspace.api.model.sourcesResponce.Source
import com.example.workspace.api.model.sourcesResponce.SourcesResponse
import com.example.workspace.databinding.FragmentHomeBinding
import com.example.workspace.ui.workspaces.WorkpsacesFragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    val apikey = "76dead7874004ebe922e3d9575a505bb"
     lateinit var viewBinding :FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      viewBinding = FragmentHomeBinding.inflate(inflater,container,false)
       return  viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         loadWorkspacesSource()


    }

    fun  changeWorkspaceFragment(source: Source){
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,WorkpsacesFragment.getInstance(source))
            .commit()
    }

    private fun loadWorkspacesSource() {
        showLoadingLayout()
        ApiManager
            .getApis()
            .getSources(ApiConstant.apikey)
            .enqueue(object :Callback<SourcesResponse>{
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                   //viewBinding.loddingIndicator.visibility = View.GONE
                    viewBinding.loddingIndicator.isVisible = false

                    if (response.isSuccessful){
                        bindSourcesInTabLayout(response.body()?.sources)
                    }else{
                        val gson = Gson()
                         val errorResponse =
                         gson.fromJson( response.errorBody()?.string(), SourcesResponse::class.java)
                          showErrorLayout(errorResponse.message)
                    }

                }

                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    viewBinding.loddingIndicator.isVisible = false
                    showErrorLayout(t.localizedMessage)
                }

            })

    }

    private fun showLoadingLayout(){
        viewBinding.loddingIndicator.isVisible = true
        viewBinding.errorLayout.isVisible=false

    }

    private fun showErrorLayout(message: String?) {
        viewBinding.errorLayout.isVisible = true
        viewBinding.loddingIndicator.isVisible=false
        viewBinding.errorMessage.text = message

    }

    fun bindSourcesInTabLayout(sourcesList  :List<Source?>? ){

         sourcesList?.forEach{ source ->
          val tab = viewBinding.tabLayout.newTab()



          tab.text = source?.name
             tab.tag =source
             //tab.setTag(source)
          viewBinding.tabLayout.addTab(tab)
             val layoutParams = LinearLayout.LayoutParams(tab.view.layoutParams)
             layoutParams.marginEnd=12
             layoutParams.marginStart=12
             layoutParams.topMargin=22
             tab.view.layoutParams =layoutParams

         }

        viewBinding.tabLayout
            .addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                   val source  = tab?.tag as Source
                    changeWorkspaceFragment(source)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {


                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val source  = tab?.tag as Source
                    changeWorkspaceFragment(source)

                }


            })


    }


}