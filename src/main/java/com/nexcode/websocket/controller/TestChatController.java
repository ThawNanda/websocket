package com.nexcode.websocket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.websocket.model.message.ChatMessage;
import com.nexcode.websocket.model.message.MessageType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class TestChatController {

	private final SimpMessagingTemplate simpMessagingTemplate;

	@PostMapping
	public ResponseEntity<?> publicChat() {
		sendMessaging();
		return null;
	}

	// @MessageMapping("/private-message")
	private void sendMessaging() {
		ChatMessage message = new ChatMessage();
		message.setContent("Hello");
		message.setSender("Hello");
		message.setType(MessageType.CHAT);

		simpMessagingTemplate.convertAndSendToUser("/topic" , "/public", message);
	}
}
