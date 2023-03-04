package com.example.workspace.ui.workspaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.workspace.R
import com.example.workspace.api.model.workspacesResponce.Workspaces
import com.example.workspace.databinding.ItemWorkspaceBinding

class WorkSpaceAdapter(var items:List<Workspaces?>?):RecyclerView.Adapter<WorkSpaceAdapter.ViewHolder>() {

    class ViewHolder(val viewBinding:ItemWorkspaceBinding)
        :RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
           val viewBinding = ItemWorkspaceBinding.inflate(
               LayoutInflater.from(parent.context)
               ,parent,false
           )
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = items?.get(position)
        holder.viewBinding.auther.text = item?.author
        holder.viewBinding.title.text = item?.title
        holder.viewBinding.time.text = item?.publishedAt
        Glide.with(holder.itemView)
            .load(item?.urlToImage)
            .placeholder(R.drawable.ic_image)
            .into(holder.viewBinding.image)




    }

    override fun getItemCount(): Int = items?.size?:0
    fun changeData(articles: List<Workspaces?>?) {
        items = articles
        notifyDataSetChanged()

    }


}