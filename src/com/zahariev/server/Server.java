package com.zahariev.server;

import com.zahariev.messages.handlers.MessageProcessor;
import com.zahariev.messages.handlers.PongMessageHandler;
import com.zahariev.messages.handlers.RequestWeatherMessageHandler;
import com.zahariev.messages.messages.PongMessage;
import com.zahariev.messages.messages.RequestWeatherMessage;
import com.zahariev.session.IOSession;
import com.zahariev.session.events.IOSessionEventArgs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Runnable
{
    private ServerSocket serverSocket;
    private Set<IOSession> clients = new HashSet<>();

    private MessageProcessor messageProcessor = new MessageProcessor();

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public Server()
    {
        setupMessageHandlers();
    }

    public void start()
    {
        createSocket();

        isRunning.set(true);
        Thread serverThread = new Thread(this, "Server Thread");
        serverThread.start();
    }

    public void stop()
    {
        isRunning.set(false);

        for(IOSession clientSession : clients)
        {
            clientSession.close();
        }

        try
        {
            serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        listenForClients();
    }

    private void setupMessageHandlers()
    {
        messageProcessor.addMessageHandler(RequestWeatherMessage.class, new RequestWeatherMessageHandler());
        messageProcessor.addMessageHandler(PongMessage.class,new PongMessageHandler());
    }

    private void createSocket()
    {
        try
        {
            serverSocket = new ServerSocket(667);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void listenForClients()
    {
        System.out.println("Listening for clients");

        while (isRunning.get())
        {
            IOSession clientSession = acceptClient();
            clientSession.socketDisconnected.addListener(this::onClientDisconnected);
            clients.add(clientSession);
            new Thread(clientSession).start();
        }
    }

    private IOSession acceptClient()
    {
        IOSession clientSession = null;

        try
        {
            Socket socket = serverSocket.accept();

            System.out.println("New client connected: " + socket);

            clientSession = new IOSession(socket, messageProcessor);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return clientSession;
    }

    private void onClientDisconnected(IOSessionEventArgs eventArgs)
    {
        clients.remove(eventArgs.getSession());
        System.out.println("Client " + eventArgs.getSession() + " disconnected.");
    }

    public static void main(String[] args)
    {
	    new Server().start();
    }
}
