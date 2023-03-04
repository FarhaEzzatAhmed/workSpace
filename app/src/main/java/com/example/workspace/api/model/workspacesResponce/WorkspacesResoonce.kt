package com.example.workspace.api.model.workspacesResponce

import com.google.gson.annotations.SerializedName

 class WorkspacesResoonce(

	 @field:SerializedName("totalResults")
	val totalResults: Int? = null,

	 @field:SerializedName("articles")
	val articles: List<Workspaces?>? = null,

	 @field:SerializedName("status")
	val status: String? = null,

	 @field:SerializedName("message")
      val message: String? = null,

	 @field:SerializedName("code")
	 val code: String? = null
)