package com.example.hello.Modal_Class;

public class Messages {
    String message, messageId, senderTd, image,seen,phoneno;
    private Long timestamp;
    private int feeling = -1;

    public Messages() {
    }

    public Messages(String message, String senderTd, Long timestamp) {
        this.message = message;
        this.senderTd = senderTd;
   //     this.seen = seen;
        this.timestamp = timestamp;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderTd() {
        return senderTd;
    }

    public void setSenderTd(String senderTd) {
        this.senderTd = senderTd;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}
