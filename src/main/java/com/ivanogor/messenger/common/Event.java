package com.ivanogor.messenger.common;

import com.ivanogor.messenger.server.Session;

public class Event {
    public Session session;
    public Packet packet;

    public Event(Session session, Packet packet) {
        this.session = session;
        this.packet = packet;
    }
}