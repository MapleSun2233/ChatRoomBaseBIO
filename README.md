### 多人聊天室
技术栈：多线程、swing、socket

运行环境：jdk1.8、 mysql5.7
#### config文件配置解析
1. port 服务端socket端口
2. ip 数据库ip地址
3. username 数据库用户名
4. password 数据库密码
#### 如何运行此项目
1. 将ChatRoom/ChatRoomServer/src/database/sql/init.sql 在mysql中执行，初始化数据库
2. 配置ChatRoom/ChatRoomServer/src/config.properties中的socket服务端口和数据库设置
3. 分别启动Server和Client即可