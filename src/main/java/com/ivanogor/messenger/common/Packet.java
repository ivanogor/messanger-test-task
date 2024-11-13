package com.ivanogor.messenger.common;

import java.io.*;
import java.util.*;
import java.util.function.*;

public abstract class Packet {
    private static final Map<String, Supplier<Packet>> typeMap = Map.of(
        EchoPacket.type, EchoPacket::new,
        HiPacket.type, HiPacket::new,
        ByePacket.type, ByePacket::new,
        MessagePacket.type, MessagePacket::new,
        ListPacket.type, ListPacket::new
    );

    public abstract String getType();

    public abstract void writeBody(PrintWriter writer) throws Exception;

    public abstract void readBody(BufferedReader reader) throws Exception;

    public void writePacket(PrintWriter writer) {
        try {
            writer.println( getType() );
            writeBody(writer);
        } 
        catch(Exception x) { throw new RuntimeException(x); }
    }

    public static Packet readPacket(BufferedReader reader) {
        try {
            var type = reader.readLine();
            if(type == null) type = "";
            var packetSupplier = typeMap.get(type);
            if(packetSupplier == null) {
                System.out.println("Unrecognized message type '" + type + "'");
                return null;
            }
            
            var packet = packetSupplier.get();
            packet.readBody(reader);
            return packet;
        } 
        catch(Exception x) { throw new RuntimeException(x); }
    }

    public String readText(BufferedReader reader) throws Exception {
        StringBuilder text = new StringBuilder();
        for(;;) {
            var s = reader.readLine();
            if(s.isEmpty()) break;
            
            if(!text.isEmpty()) text.append("\n");
            text.append(s);
        }
        return text.toString();
    }
}