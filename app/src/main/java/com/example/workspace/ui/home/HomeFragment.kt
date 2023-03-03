package com.example.workspace.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.workspace.R
import com.example.workspace.api.model.ApiConstant
import com.example.workspace.api.model.ApiManager
import com.example.workspace.api.model.Source
import com.example.workspace.api.model.SourcesResponse
import com.example.workspace.databinding.FragmentHomeBinding
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
                        gson.fromJson( response.errorBody()?.string(),SourcesResponse::class.java)
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
          viewBinding.tabLayout.addTab(tab)

         }



    }


}