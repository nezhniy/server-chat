package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import ru.itsjava.domain.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer{
    private final Socket socket;
    private final ServerService serverService;
    private User user;

    private final User userNotExpect = new User("NAME", "0000");

    @Override
    public void run() {
        System.out.println("Client connected");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String messageFromClient;
        try {
            if (authorization(bufferedReader)){
                serverService.addObserver(this);
                while((messageFromClient = bufferedReader.readLine()) != null){
                    System.out.println(user.getName() + ":" + messageFromClient);
                    serverService.notifyObserverExpectMe(this, (user.getName() + ":" + messageFromClient));
                    if (messageFromClient.equals("exit")){
                        break;
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authorization(BufferedReader bufferedReader) throws IOException {
        String authorizationMessage;
        while ((authorizationMessage = bufferedReader.readLine()) != null){
            if (authorizationMessage.startsWith("!autho!")){
                String login = authorizationMessage.substring(7).split(":")[0];
                String password = authorizationMessage.substring(7).split(":")[1];
                user = new User(login, password);
                return true;
            }
        }
        return false;
    }

    @Override
    public void notifyMe(String message) {
        PrintWriter clientWriter = null;
        try {
            clientWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clientWriter == null){
            clientWriter.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            clientWriter.println(message);
            clientWriter.flush();
        }
    }
}
