package com.zahariev.session.events;

import com.zahariev.events.EventArgs;
import com.zahariev.session.IOSession;

public class IOSessionEventArgs extends EventArgs
{
	private IOSession session;

	public IOSessionEventArgs(IOSession session)
	{
		this.session = session;
	}

	public IOSession getSession()
	{
		return session;
	}
}
