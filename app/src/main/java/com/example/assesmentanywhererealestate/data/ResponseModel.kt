package com.example.assesmentanywhererealestate.data

import com.google.gson.annotations.SerializedName

data class ResponseModel(@SerializedName("RelatedTopics") val relatedTopics: List<RelatedTopicResponse>)
