package com.zahariev.session.events;

import com.zahariev.messages.messages.IMessage;
import com.zahariev.session.IOSession;

public class IOSessionMessageEventArgs extends IOSessionEventArgs
{
	private IMessage message;

	public IOSessionMessageEventArgs(IOSession session, IMessage message)
	{
		super(session);
		this.message = message;
	}

	public IMessage getMessage()
	{
		return message;
	}
}
