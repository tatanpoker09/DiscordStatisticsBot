package com.eilers.tatanpoker09;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.Objects;

public class WebSocket implements Runnable{
    private static final String MESSAGE_ADDED_EVENT = "message_added_broadcast";

    private Socket webSocket;

    public WebSocket() {

    }

    public void start(){
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.upgrade = false;
            opts.transports = new String[]{"websocket"};
            Socket socket = IO.socket("http://localhost", opts);
            socket.on(Socket.EVENT_CONNECT, objects -> {
                System.out.println("Socket connected");
            });
            socket.connect();
            socket.emit("");
            this.webSocket = socket;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendBroadcast(String event, Object[] payload){
        webSocket.emit(event, payload);
    }

    public void sendMessageAddedBroadcast(){
        String[] payload = new String[]{

        };
        sendBroadcast(MESSAGE_ADDED_EVENT, payload);
    }

    @Override
    public void run() {
        start();
    }
}
