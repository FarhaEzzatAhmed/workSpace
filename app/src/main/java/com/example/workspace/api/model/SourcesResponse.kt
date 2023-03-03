package com.example.workspace.api.model

import com.google.gson.annotations.SerializedName

 class SourcesResponse(

     @field:SerializedName("sources")
     val sources: List<Source?>? = null,

     @field:SerializedName("status")
     val status: String? = null,

     @field:SerializedName("code")
     val code: String? = null,
     @field:SerializedName("message")
     val message: String? = null,
)