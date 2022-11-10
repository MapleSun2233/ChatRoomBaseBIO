package constant;

/**
 * 消息类型
 */
public enum MsgType {
    /**
     * 群消息
     */
    PUBLIC_MSG,
    /**
     * 私密消息
     */
    PRIVATE_MSG,
    /**
     * 用户注册消息
     */
    REGISTER_USER,
    /**
     * 注册成功消息
     */
    REGISTER_SUCCESS,
    /**
     * 注册失败消息
     */
    REGISTER_FAIL,
    /**
     * 用户登录消息
     */
    LOGIN_USER,
    /**
     * 登录成功消息
     */
    LOGIN_SUCCESS,
    /**
     * 登录失败消息
     */
    LOGIN_FAIL,
    /**
     * 用户更新消息
     */
    UPDATE_USER,
    /**
     * 更新成功消息
     */
    UPDATE_SUCCESS,
    /**
     * 更新失败消息
     */
    UPDATE_FAIL,
    /**
     * 密码找回消息
     */
    RETRIEVE_USER,
    /**
     * 找回成功消息
     */
    RETRIEVE_SUCCESS,
    /**
     * 找回失败消息
     */
    RETRIEVE_FAIL,
    /**
     * 用户注销消息
     */
    DELETE_USER,
    /**
     * 注销成功消息
     */
    DELETE_SUCCESS,
    /**
     * 用户列表消息
     */
    USER_LIST,
    /**
     * socket异常
     */
    SOCKET_EXCEPTION
}
