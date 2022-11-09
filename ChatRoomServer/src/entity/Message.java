package entity;

import constant.MsgType;

import java.io.Serializable;

/**
 * 消息实体
 */
public class Message implements Serializable {
    private final MsgType type;
    private final String content;

    private Message(MsgType type, String content) {
        this.type = type;
        this.content = content;
    }

    public static Message build(MsgType type, String content) {
        return new Message(type, content);
    }

    public MsgType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
