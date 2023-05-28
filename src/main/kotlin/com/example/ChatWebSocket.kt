package com.example


import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import jakarta.inject.Singleton
import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.function.Predicate


@ServerWebSocket("/ws/chat/{id1}/{id2}")
@Secured(SecurityRule.IS_ANONYMOUS)
@Singleton
class ChatWebSocket(private val broadcaster: WebSocketBroadcaster, private val chatMessageRepository: ChatMessageRepository) {

    private fun log(event: String, session: WebSocketSession, id1: String, id2: String) {
        val name_2 = chatMessageRepository.findById(id1)?.firstName.toString()
        LOG.info("* WebSocket: {} received for session {} from '{}' regarding '{}'",
            event, session.id, id2, name_2)
    }

    @OnOpen
    fun onOpen(id1: String, id2: String, session: WebSocketSession): Publisher<String> {
        log("onOpen", session, id1, id2)
        val name_2 = chatMessageRepository.findById(id1)?.firstName.toString()
        val message = String.format("")
//        val messages = chatMessageRepository.findBySenderIdAndReceiverId(id1, id2)
//
//        // Send each message to the client
//        messages.forEach {
//            session.send(String.format("[%s] %s", name_2, it.message))
//        }

        return broadcaster.broadcast(message, isValid(id1, id2))
    }

    @OnMessage
    fun onMessage(id1: String, id2: String, message: String, session: WebSocketSession): Publisher<String> {
        val mapper = ObjectMapper()
        val messageObject = mapper.readValue(message, ChatMessageDTO::class.java)
        log("onMessage", session, id1, id2)
        val name_2 = chatMessageRepository.findById(id1)?.firstName.toString()
        val id = ObjectId().toString()
        val chatMessage = ChatMessage(
            id = id ,
            senderId = id1,
            receiverId = id2,
            message = messageObject.text,
            sentTime = Instant.now(),
            referredMessageId = messageObject.referredMessageId.toString(),
            referredMessageText = messageObject.referredMessageText.toString()
        )
        val gson = Gson()
        val chatMessageJson = gson.toJson(chatMessage)
        chatMessageRepository.save(chatMessage)
        return broadcaster.broadcast(String.format(chatMessageJson), isValid(id1, id2))
    }

    @OnClose
    fun onClose(id1: String, id2: String, session: WebSocketSession): Publisher<String> {
        log("onClose", session, id1, id2)
        val name_2 = chatMessageRepository.findById(id1)?.firstName.toString()
        return broadcaster.broadcast(String.format(""), isValid(id1, id2))
    }

    private fun isValid(id1: String, id2: String): Predicate<WebSocketSession> {
        return Predicate { session ->
            val uriVars = session.uriVariables
            val session_id1 = uriVars.get("id1", String::class.java, null)
            val session_id2 = uriVars.get("id2", String::class.java, null)
            return@Predicate ((session_id1 == id1 && session_id2 == id2) || (session_id1 == id2 && session_id2 == id1))
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ChatWebSocket::class.java)
    }
}
