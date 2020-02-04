![](https://lh3.googleusercontent.com/-Zu1tFauo9r4/VuDuhO0cp_I/AAAAAAAAOHU/NqgaCg4tfso/s0/chat-socket-proj-banner.png)

[![Gitter chat](https://travis-ci.com/sontx/chat-socket.svg?branch=master)](https://travis-ci.com/sontx/chat-socket)

**chat-socket** is a simple chat application includes chat client and chat server.
Support multiple users, private chat, and personal status.
This project is based on TCP socket, multi-threading, JavaFx, Java Swing, Java serialization, and JSON.

<a href="https://youtu.be/tIEG-Q6liXw">
<img src="https://3.bp.blogspot.com/-4mgFXugXUps/WzcobCOOhOI/AAAAAAAAVOY/y9D8YMwETGUIrElK5rJy_XT2l_6iO1s7QCLcBGAs/s1600/client-friendlist.PNG">
</a>

## Prerequisites
To compile this project, you need to install these things:

- [jdk8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [maven](https://maven.apache.org/download.cgi)

## Installing
Build (binary file will be placed in **target** directory):
```bash
mvn clean compile assembly:single
```

Run (will ask you for working mode):
```bash
mvn exec:java
```

## Usage
Both server and client are in **chatsocket-x.y-SNAPSHOT-jar-with-dependencies.jar** (to get this jar file you need to build this project).
1. Server app: `java -jar chatsocket.jar --mode=server`
2. Client app: `java -jar chatsocket.jar --mode=client`

App settings will be saved in **app.json** file
```json
{
  "modified" : 1580791811962,
  "settings" : [ {
    "key" : "client",
    "data" : {
      "serverIp" : "127.0.0.1",
      "serverPort" : 3393,
      "loggedUserName" : "sontx"
    }
  }, {
    "key" : "server",
    "data" : {
      "ip" : "127.0.0.1",
      "port" : 3393
    }
  } ]
}
``` 

### Server
The server needs to listen to an IP and a port number, other clients will connect to this address.
Fill up these boxes and click on **Start** button to start the server.

![](https://1.bp.blogspot.com/-kB7oA2W7bcc/WzcodYjgBtI/AAAAAAAAVOs/JBM-hmbCSoE9aWaZYSl77k0C0Ggm-kkZgCLcBGAs/s1600/server.PNG)

All users data will be saved in **user.json** file.
```json
[ {
  "id" : "7453f7f5105547179f0845cd03fe4eda",
  "username" : "sontx",
  "passwordHash" : "7c4a8d09ca3762af61e59520943dc26494f8941b",
  "profile" : {
    "displayName" : "Tran Xuan Son",
    "status" : "I'm just a beginner"
  }
} ]
```
### Client
1. Connect to the server.

![](https://3.bp.blogspot.com/-BfelkTjAyt0/WzcobEPCZ8I/AAAAAAAAVOc/NbLfhTq8yfYFisydCUs9KiessLw4w3P0ACLcBGAs/s1600/client-connection.PNG)

2. Then login with your account

![](https://4.bp.blogspot.com/-V-KG-eGL84Q/Wzcob9l9eII/AAAAAAAAVOg/3d5hNPowqdMVQSTa5MN7nwj-WgkO_k7dgCLcBGAs/s1600/client-login.PNG)

3. Or register a new one.

![](https://2.bp.blogspot.com/-j4opAkxDQPU/Wzcocwr_xPI/AAAAAAAAVOo/UYAr6J84gjIjYUwskm0oGJ3rBPda8ZWNwCLcBGAs/s1600/client-register.PNG)

4. Other users will be shown in your friend list.

![](https://3.bp.blogspot.com/-4mgFXugXUps/WzcobCOOhOI/AAAAAAAAVOY/y9D8YMwETGUIrElK5rJy_XT2l_6iO1s7QCLcBGAs/s1600/client-friendlist.PNG)

5. To update your userProfile, double-click to the avatar.

![](https://3.bp.blogspot.com/-CwPvtWoHAY4/WzcocQqJQAI/AAAAAAAAVOk/mfAu94v3EOwlxoqCxy8lYVmP593h_bXvQCLcBGAs/s1600/client-userProfile.PNG)

6. Double-click to any online friend to chat

![](https://1.bp.blogspot.com/-MrJ4UuYQk7Y/WzcobCL1rSI/AAAAAAAAVOU/1M8NUAGLMd8TqbU47dGvv--fFV0BYYENwCLcBGAs/s1600/client-chatting.PNG)

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Dependencies

 - [**common-lang3**](https://commons.apache.org/proper/commons-lang/).
 - [**java-json**](http://www.java2s.com/Code/Jar/j/Downloadjavajsonjar.htm)
 - [**jackson**](https://github.com/FasterXML/jackson)
 - [**commons-text**](https://commons.apache.org/proper/commons-text/)
 - [**commons-cli**](https://commons.apache.org/proper/commons-cli/)
 - [**lombok**](https://projectlombok.org/)
 - [**log4j**](https://logging.apache.org/log4j)
 - [**guava**](https://github.com/google/guava) 
 - [**jdk 1.8**](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Author
Developed by sontx:

 - Home: [www.code4bugs.com](https://code4bugs.com)
 - Email: <a href="mailto:xuanson33bk@gmail.com">xuanson33bk@gmail.com</a>
 - Twitter: [@sontx0](https://twitter.com/sontx0)

## License
[MIT](https://github.com/sontx/chat-socket/blob/master/LICENSE)
