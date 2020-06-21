package com.zahariev.messages.handlers;

import com.zahariev.messages.messages.IMessage;
import com.zahariev.session.IOSession;

public interface IMessageHandler
{
	void handleMessage(IOSession session, IMessage message);
}
