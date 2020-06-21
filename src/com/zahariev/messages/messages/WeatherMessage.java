package com.zahariev.messages.messages;

public class WeatherMessage implements IMessage
{
	private String weather;

	public WeatherMessage(String weather)
	{
		this.weather = weather;
	}

	public String getWeather()
	{
		return weather;
	}
}
