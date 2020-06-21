package com.zahariev.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Event<T extends EventArgs>
{
	private List<Consumer<T>> listeners = new ArrayList<>();

	public void addListener(Consumer<T> listener)
	{
		listeners.add(listener);
	}

	public void clear()
	{
		listeners.clear();
	}

	public void invoke(T eventArgs)
	{
		List<Consumer<T>> listenersCopy = new ArrayList<>(listeners);
		for(int i = listenersCopy.size() - 1; i >= 0; --i)
		{
			listenersCopy.get(i).accept(eventArgs);
		}
	}
}
