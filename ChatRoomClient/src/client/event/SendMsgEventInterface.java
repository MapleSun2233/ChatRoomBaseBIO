package client.event;

import constant.CommonConstant;
import constant.MsgType;
import entity.Message;
import utils.CommonUtil;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;

public interface SendMsgEventInterface {
    /**
     * 发送消息
     *
     * @param clientOS       输出流
     * @param content        聊天内容组件
     * @param sendMsgContent 发送内容组件
     * @param contactTo      私信目标
     */
    default void sendMsg(ObjectOutputStream clientOS, JTextArea content, JTextArea sendMsgContent, JComboBox<String> contactTo) {
        String str = sendMsgContent.getText().trim();
        if (CommonUtil.isNotEmpty(str)) {
            try {
                String contactToWho = (String) contactTo.getSelectedItem();
                if (CommonUtil.isEquals(CommonConstant.ALL, contactToWho)) {
                    clientOS.writeObject(Message.build(MsgType.PUBLIC_MSG, str));
                } else {
                    clientOS.writeObject(Message.build(MsgType.PRIVATE_MSG, CommonUtil.join(contactToWho, CommonConstant.AND, str)));
                    //更新聊天内容
                    content.setText(CommonUtil.join(content.getText(), "本账户发送给", contactToWho, "的私信：", str, CommonConstant.NEW_LINE));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // 重置发送框
        sendMsgContent.setText("");
    }
}
