package com.zahariev.messages.handlers;

import com.zahariev.messages.messages.IMessage;
import com.zahariev.session.IOSession;

public class PongMessageHandler implements IMessageHandler
{
	@Override
	public void handleMessage(IOSession session, IMessage message)
	{
		System.out.println("Client is alive.");
	}
}
