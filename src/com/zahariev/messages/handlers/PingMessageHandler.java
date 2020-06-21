package com.zahariev.messages.handlers;

import com.zahariev.messages.messages.IMessage;
import com.zahariev.messages.messages.PongMessage;
import com.zahariev.session.IOSession;

public class PingMessageHandler implements IMessageHandler
{

	@Override
	public void handleMessage(IOSession session, IMessage message)
	{
		System.out.println("Ping from server");
		session.sendMessage(new PongMessage());
	}
}

