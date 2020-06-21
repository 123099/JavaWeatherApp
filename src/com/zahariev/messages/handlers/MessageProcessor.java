package com.zahariev.messages.handlers;

import com.zahariev.messages.messages.IMessage;

import java.util.HashMap;
import java.util.Map;

public class MessageProcessor
{
	private Map<Class<?>, IMessageHandler> messageHandlers = new HashMap<>();

	public <T extends IMessage> void addMessageHandler(Class<T> messageType, IMessageHandler handler)
	{
		messageHandlers.put(messageType, handler);
	}

	public <T extends IMessage> IMessageHandler getHandler(Class<T> messageType)
	{
		return messageHandlers.getOrDefault(messageType, null);
	}

	public void clear()
	{
		messageHandlers.clear();
	}
}
