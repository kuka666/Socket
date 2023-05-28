package com.example


import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.Instant

data class ChatMessage @BsonCreator constructor(
    @BsonProperty("id") val id: String?,
    @BsonProperty("senderId") val senderId: String,
    @BsonProperty("receiverId") val receiverId: String,
    @BsonProperty("message") val message: String,
    @BsonProperty("sentTime") val sentTime: Instant,
    @BsonProperty("referredMessageId") val referredMessageId: String,
    @BsonProperty("referredMessageText") val referredMessageText: String
){
    constructor(senderId: String, receiverId: String, message: String, sentTime: Instant, referredMessageId: String, referredMessageText: String) : this(null, senderId, receiverId, message, sentTime, referredMessageId, referredMessageText)
}
data class ChatMessageDTO(
    @JsonProperty("text") val text: String,
    @JsonProperty("referredMessageText") val referredMessageText: String?,
    @JsonProperty("referredMessageId") val referredMessageId: String?
)
