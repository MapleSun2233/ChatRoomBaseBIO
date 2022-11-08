package utils;

import java.io.Serializable;

public class Message implements Serializable {
    private MsgType type;
    private String content;
    public Message(MsgType type,String content){
        this.type = type;
        this.content = content;
    }

    public MsgType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
