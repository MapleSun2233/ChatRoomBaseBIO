### 多人聊天室
技术栈：多线程、swing、BIO

运行环境：jdk1.8、 mysql8
#### config文件配置解析
1. port 服务端socket监听端口
2. url 数据库连接url，将localhost替换为自己的数据库IP地址即可
```properties
url = jdbc:mysql://localhost:3306...
```
3. username 数据库用户名
4. password 数据库密码
5. recommendScreenWidth 建议的最佳分辨率
6. recommendScreenHeight 建议的最佳分辨率
#### 如何运行服务端
1. 将ChatRoom/ChatRoomServer/src/database/sql/init.sql 在mysql中执行，初始化数据库
2. 配置ChatRoom/ChatRoomServer/src/config.properties中的socket服务端口和数据库设置
3. 启动Server，等待Client连接
#### 如何运行客户端
1. 启动Client
2. 根据服务端所在机器的IP地址和指定的监听端口，修正服务器地址
3. 正常使用