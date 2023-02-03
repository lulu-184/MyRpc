package com.jxj.netty.client;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description
 */

public class ClientRequest {
    private final Long id;
    private Object content;
    private final AtomicLong aid = new AtomicLong(1);
    public ClientRequest() {
        id = aid.incrementAndGet();
    }
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getId() {
        return id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

}
