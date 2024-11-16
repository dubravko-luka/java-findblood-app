package com.example.findblood;

public class Chat {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;
    
    private String senderName;
    private String message;
    private String timestamp;
    private boolean isSent;

    public Chat(String senderName, String message, String timestamp, boolean isSent) {
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isSent() {
        return isSent;
    }
}
