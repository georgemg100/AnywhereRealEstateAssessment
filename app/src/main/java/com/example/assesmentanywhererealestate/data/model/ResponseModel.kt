package com.example.assesmentanywhererealestate.data.model

import com.google.gson.annotations.SerializedName

data class ResponseModel(@SerializedName("RelatedTopics") val relatedTopics: List<RelatedTopicResponse>)
