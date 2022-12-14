package ru.itsjava.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerServiceImpl implements ServerService, Observable{
    public final static int PORT = 8081;
    public final List<Observer> observers = new ArrayList<>();

    @Override
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("== SERVER STARTS ==");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    Thread thread = new Thread(new ClientRunnable(socket, this));
                    thread.start();
                }
            }
        } catch (IOException e){
            serverSocket.close();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String message) {
        for (Observer observer: observers){
            observer.notifyMe(message);
        }
    }

    @Override
    public void notifyObserverExpectMe(Observer observerEx, String message) {
        for (Observer observer: observers){
            if (observer == observerEx){
                continue;
            }
            observer.notifyMe(message);
        }
    }
}
