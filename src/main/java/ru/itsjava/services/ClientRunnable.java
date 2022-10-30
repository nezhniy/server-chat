package ru.itsjava.services;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer{
    private final Socket socket;

    @Override
    public void run() {
        System.out.println("Client connected!");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String messageFromClient;
        try {
            while((messageFromClient = bufferedReader.readLine()) != null){
                System.out.println(messageFromClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyMe(String message) {
        PrintWriter clientWriter = null;
        try {
            clientWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientWriter.println(message);
        clientWriter.flush();
    }
}
