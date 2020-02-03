![](https://lh3.googleusercontent.com/-Zu1tFauo9r4/VuDuhO0cp_I/AAAAAAAAOHU/NqgaCg4tfso/s0/chat-socket-proj-banner.png)

[![Gitter chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/chat-socket/Lobby#)

**chat-socket** is a simple chat application includes chat client and chat server.
Support multiple users, private chat, and personal status.
This project is based on TCP socket, multithreading, Java serialization, and JSON.

<a href="https://youtu.be/tIEG-Q6liXw">
<img src="https://3.bp.blogspot.com/-4mgFXugXUps/WzcobCOOhOI/AAAAAAAAVOY/y9D8YMwETGUIrElK5rJy_XT2l_6iO1s7QCLcBGAs/s1600/client-friendlist.PNG">
</a>

## Installation
Download the [binary file](https://github.com/sontx/chat-socket/releases) or clone this project and use your favorite IDE to
customize then rebuild by:

1. Run `install.bat` in [scripts](https://github.com/sontx/chat-socket/tree/master/scripts) folder.
1. `mvn clean compile assembly:single` (the binary file will be placed in **target** folder)

> This project requires [jdk 1.8 or later](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

## Usage
Both server and client are in **homechat.jar**.
1. Server app: run `server.bat`/`server.sh` or `java -jar homechat.jar --mode=server`
2. Client app: run `client.bat`/`client.sh` or `java -jar homechat.jar --mode=client`

### Server
The server needs to listen to an IP and a port number, other clients will connect to this address.
Fill up these boxes and click on **Start** button to start the server.

![](https://1.bp.blogspot.com/-kB7oA2W7bcc/WzcodYjgBtI/AAAAAAAAVOs/JBM-hmbCSoE9aWaZYSl77k0C0Ggm-kkZgCLcBGAs/s1600/server.PNG)

All users data will be saved in **user.json** file.

![](https://1.bp.blogspot.com/-3jSUQe1DRLY/WzcuV6DxROI/AAAAAAAAVPE/Ebh3KjveqG4kPMdfOvM2-mKjfjSpwMAbACLcBGAs/user-json.PNG)

### Client
1. Connect to the server.

![](https://3.bp.blogspot.com/-BfelkTjAyt0/WzcobEPCZ8I/AAAAAAAAVOc/NbLfhTq8yfYFisydCUs9KiessLw4w3P0ACLcBGAs/s1600/client-connection.PNG)

2. Login to the system.

![](https://4.bp.blogspot.com/-V-KG-eGL84Q/Wzcob9l9eII/AAAAAAAAVOg/3d5hNPowqdMVQSTa5MN7nwj-WgkO_k7dgCLcBGAs/s1600/client-login.PNG)

3. Or register a new account.

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
 - [**eventbus**](https://github.com/sontx/eventbus-1)(desktop supported), original is [here](https://github.com/greenrobot/EventBus) 
 - [**jdk 1.8**](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

## Author
Developed by sontx/noem, some useful information:

 - Home: [www.code4bugs.com](https://code4bugs.com)
 - Blog: [https://sontx.blogspot.com](https://sontx.blogspot.com)
 - Email: <a href="mailto:xuanson33bk@gmail.com">xuanson33bk@gmail.com</a>
 - Twitter: [@sontx0](https://twitter.com/sontx0)

## License
[MIT](https://github.com/sontx/chat-socket/blob/master/LICENSE)
