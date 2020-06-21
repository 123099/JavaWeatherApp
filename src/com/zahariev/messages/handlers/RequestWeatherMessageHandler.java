package com.zahariev.messages.handlers;

import com.zahariev.messages.messages.IMessage;
import com.zahariev.messages.messages.WeatherMessage;
import com.zahariev.session.IOSession;

public class RequestWeatherMessageHandler implements IMessageHandler
{
	@Override
	public void handleMessage(IOSession session, IMessage message)
	{
		WeatherMessage weatherMessage = new WeatherMessage("The weather is nice and sunny!");
		session.sendMessage(weatherMessage);
	}
}
