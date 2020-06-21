package com.zahariev.messages.handlers;

import com.zahariev.messages.messages.IMessage;
import com.zahariev.messages.messages.WeatherMessage;
import com.zahariev.session.IOSession;

public class WeatherMessageHandler implements IMessageHandler
{
	@Override
	public void handleMessage(IOSession session, IMessage message)
	{
		WeatherMessage weatherMessage = (WeatherMessage)message;
		System.out.println(weatherMessage.getWeather());
	}
}
