package com.example.workspace.ui.workspaces

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.workspace.api.model.ApiConstant
import com.example.workspace.api.model.ApiManager
import com.example.workspace.api.model.sourcesResponce.Source
import com.example.workspace.api.model.sourcesResponce.SourcesResponse
import com.example.workspace.api.model.workspacesResponce.Workspaces
import com.example.workspace.api.model.workspacesResponce.WorkspacesResoonce
import com.example.workspace.databinding.FragmentWorkspacesBinding
import com.example.workspace.ui.workspaceDetails.WorkspaceDetailsActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkpsacesFragment :Fragment() {

    companion object{

      fun  getInstance(source: Source):WorkpsacesFragment{
          val newWorkpsacesFragment =WorkpsacesFragment()
          newWorkpsacesFragment.source = source
          return newWorkpsacesFragment

        }

    }
 lateinit var source: Source



     lateinit var viewBinding:FragmentWorkspacesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentWorkspacesBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           initRecyclerView()
           getWorkspaces()

    }

    val workspacesAdapter = WorkSpaceAdapter(null)
    private fun initRecyclerView() {
        viewBinding.workspaceRecycler.adapter =workspacesAdapter
        workspacesAdapter.onClickListener =object :WorkSpaceAdapter.OnClickListener{
            override fun onWorkspaceClick(workspaces: Workspaces) {
                val intent = Intent(requireContext(),WorkspaceDetailsActivity::class.java).apply {
                    putExtra("workspaceName",workspaces.title)
                    putExtra("workspaceImage",workspaces.urlToImage)
                }
                startActivity(intent)

            }


        }



    }

    private fun getWorkspaces() {
        showLoadingLayout()
        ApiManager
            .getApis()
            .getWorkspaces(ApiConstant.apikey,source.id?:"")
            .enqueue(object :Callback<WorkspacesResoonce>{
                override fun onResponse(
                    call: Call<WorkspacesResoonce>,
                    response: Response<WorkspacesResoonce>
                ) {

                    if (response.isSuccessful){
                        //we have workspaces to show
                        bindWorkspaceList(response.body()?.articles)
                        return
                    }
                        //val gson = Gson()
                        val errorResponse =
                          Gson().fromJson( response.errorBody()?.string(), SourcesResponse::class.java)
                        showErrorLayout(errorResponse.message)

                }

                override fun onFailure(call: Call<WorkspacesResoonce>, t: Throwable) {
                    showErrorLayout(t.localizedMessage)
                }


            })

    }

    private fun bindWorkspaceList(articles: List<Workspaces?>?) {
        //show Workspaces in Recycler view
         viewBinding.loddingIndicator.isVisible = false
         viewBinding.errorLayout.isVisible=false
          workspacesAdapter.changeData(articles)



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
}