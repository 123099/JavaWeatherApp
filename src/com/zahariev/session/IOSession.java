package com.zahariev.session;

import com.zahariev.events.Event;
import com.zahariev.messages.handlers.IMessageHandler;
import com.zahariev.messages.handlers.MessageProcessor;
import com.zahariev.messages.messages.IMessage;
import com.zahariev.session.events.IOSessionEventArgs;
import com.zahariev.session.events.IOSessionMessageEventArgs;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class IOSession implements Runnable, AutoCloseable
{
	public Event<IOSessionMessageEventArgs> messageReceived = new Event<>();
	public Event<IOSessionEventArgs> socketDisconnected = new Event<>();

	private Socket socket;

	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

	private MessageProcessor messageProcessor;

	private AtomicBoolean isClosed;

	public IOSession(Socket socket, MessageProcessor messageProcessor)
	{
		this.socket = socket;
		this.messageProcessor = messageProcessor;

		messageReceived.addListener(this::onMessageReceived);
		socketDisconnected.addListener(this::onSocketDisconnected);

		try
		{
			// Must create the output stream first to avoid deadlock.
			// Input stream constructor blocks until output stream flushes. Therefore, creating an input stream first on both client and server causes a deadlock.
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());

			isClosed = new AtomicBoolean(false);
		}
		catch(SocketException e)
		{
			// Indicates that we are already closed.
			// Do nothing.
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void close()
	{
		try
		{
			isClosed.set(true);
			messageReceived.clear();
			socketDisconnected.clear();

			objectOutputStream.close();
			objectInputStream.close();
			socket.close();
		}
		catch(SocketException e)
		{
			// Indicates that we are already closed.
			// Do nothing.
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendMessage(IMessage message)
	{
		if(isClosed.get())
		{
			return;
		}

		try
		{
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
		}
		catch (SocketException e)
		{
			socketDisconnected.invoke(new IOSessionEventArgs(this));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		receiveMessages();
	}

	@Override
	public String toString()
	{
		return socket.toString();
	}

	private void receiveMessages()
	{
		while(!isClosed.get())
		{
			receiveMessage();
		}
	}

	private IMessage receiveMessage()
	{
		IMessage message = null;

		try
		{
			Object object = objectInputStream.readObject();

			if(object instanceof IMessage)
			{
				message = (IMessage)object;
				messageReceived.invoke(new IOSessionMessageEventArgs(this, message));
			}
		}
		catch (SocketException | EOFException e)
		{
			socketDisconnected.invoke(new IOSessionEventArgs(this));
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return message;
	}

	private void onMessageReceived(IOSessionMessageEventArgs eventArgs)
	{
		IMessageHandler handler = messageProcessor.getHandler(eventArgs.getMessage().getClass());

		if(handler == null)
		{
			System.out.println("No handler registered to handle the message type " + eventArgs.getMessage().getClass());
			return;
		}

		handler.handleMessage(this, eventArgs.getMessage());
	}

	private void onSocketDisconnected(IOSessionEventArgs eventArgs)
	{
		close();
	}
}
