package com.example


import com.example.MongoDbConfiguration
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.result.InsertOneResult
import jakarta.inject.Singleton
import org.bson.types.ObjectId
import java.time.Instant
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux

@Singleton
class ChatMessageRepository(private val mongoClient: MongoClient, private val mongoConf: MongoDbConfiguration,) {
    private val collectionName = "chat_messages"

    fun save(message: ChatMessage): InsertOneResult {
        val id = ObjectId().toString()
        val messageToSave = message.copy(id = id)
        return mongoClient
            .getDatabase(mongoConf.name)
            .getCollection("chat", ChatMessage::class.java)
            .insertOne(messageToSave)
    }

    fun findAll(): FindIterable<ChatMessage> {
        return mongoClient
            .getDatabase(mongoConf.name)
            .getCollection("chat", ChatMessage::class.java)
            .find()
    }

    fun findById(userId: String): User? {
        return mongoClient
            .getDatabase(mongoConf.name)
            .getCollection("users", User::class.java)
            .find(Filters.eq("_id", ObjectId(userId))).firstOrNull()
    }

    fun findBySenderIdAndReceiverId(senderId: String, receiverId: String): FindIterable<ChatMessage> {
        return mongoClient
            .getDatabase(mongoConf.name)
            .getCollection("chat", ChatMessage::class.java)
            .find(
                and(
                    eq("senderId", senderId),
                    eq("receiverId", receiverId)
                )
            )
    }
}
