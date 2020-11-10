package com.lotpick.lotpick.Models;

public class ModelChat {
    String message , Receiver , Sender ,Timestamp;
    boolean isSeen;

    public ModelChat() {

    }

    public ModelChat(String message, String receiver, String sender, String timestamp, boolean isSeen) {
        this.message = message;
        this.Receiver = receiver;
        this.Sender = sender;
        this.Timestamp = timestamp;
        this.isSeen = isSeen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReciever(String receiver) {
        this.Receiver = receiver;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        this.Sender = sender;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.Timestamp = timestamp;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        this.isSeen = seen;
    }
}
