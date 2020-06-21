package com.zahariev.client;

import com.zahariev.messages.handlers.MessageProcessor;
import com.zahariev.messages.handlers.PingMessageHandler;
import com.zahariev.messages.handlers.WeatherMessageHandler;
import com.zahariev.messages.messages.PingMessage;
import com.zahariev.messages.messages.RequestWeatherMessage;
import com.zahariev.messages.messages.WeatherMessage;
import com.zahariev.session.IOSession;

import java.io.IOException;
import java.net.Socket;

public class Client
{
	private IOSession session;
	private MessageProcessor messageProcessor = new MessageProcessor();

	public void start()
	{
		setupMessageHandlers();

		try(Socket socket = new Socket("localhost",667))
		{
			System.out.println("Connected...");

			session = new IOSession(socket, messageProcessor);
			session.socketDisconnected.addListener(s -> System.out.println("Server disconnected"));
			session.sendMessage(new RequestWeatherMessage());
			session.run();
		}
		catch (IOException e)
		{
			System.out.println("The server seems to be offline.");
		}
	}

	private void setupMessageHandlers()
	{
		messageProcessor.addMessageHandler(WeatherMessage.class, new WeatherMessageHandler());
		messageProcessor.addMessageHandler(PingMessage.class, new PingMessageHandler());
	}

	public static void main(String[] args)
	{
		new Client().start();
	}
}
