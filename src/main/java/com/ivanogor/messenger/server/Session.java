package com.ivanogor.messenger.server;

import com.ivanogor.messenger.common.ByePacket;
import com.ivanogor.messenger.common.Event;
import com.ivanogor.messenger.common.Packet;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;
 
public class Session extends Thread {
    private final Socket socket;
    private PrintWriter writer;
    private final LinkedBlockingQueue<Packet> toClientQueue = new LinkedBlockingQueue<>();
    private final Thread writerThread;

    public Correspondent correspondent;
 
    public Session(Socket socket) {
        this.socket = socket;

        writerThread = new Thread(() -> {
            for(;;) {
                try {
                    var p = toClientQueue.take();
                    System.out.println("Sending message: " + p.getType());
                    p.writePacket(writer);
                } 
                catch (InterruptedException x) {
                    break;
                }
            }
        });
        
        writerThread.start();
    }

    public void send(Packet p) {
        toClientQueue.add(p);
    }
 
    public void run() {
        try {
            try (socket) {
				System.out.println("Got incoming connection");

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);
                
                for(;;) {
                    var p = Packet.readPacket(reader);

                    if( p == null || p.getType().equals(ByePacket.type) ) {
                        close();
                        return;
                    }

                    var e = new Event(this, p);
                    Dispatcher.event(e);
                }
            }
        } catch (IOException ex) {
            System.out.println("Session problem: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if(correspondent != null) correspondent.activeSession = null;
            writerThread.interrupt();
            socket.close();
        }
        catch(Exception ex) {
            System.out.println("Session closing problem: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}