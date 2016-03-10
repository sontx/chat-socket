![](https://lh3.googleusercontent.com/-Zu1tFauo9r4/VuDuhO0cp_I/AAAAAAAAOHU/NqgaCg4tfso/s0/chat-socket-proj-banner.png)
**chat-socket** is a simple chat application include chat client and chat server. Support multiple users, private chat and show personal status. User must take an account before enter to system, this step require some simple information such as username, password....This project base on tcp socket, multithreading, java serialization and json.

<a href="https://youtu.be/tIEG-Q6liXw">
<img src="https://lh3.googleusercontent.com/-rmZzcxTw5UU/VuFSsd3pYoI/AAAAAAAAOLs/djf79cFry0A/s0/Capture.PNG">
</a>

## Installation
This project require [java runtime 1.6 or higher](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html), you can use [Eclipse](https://eclipse.org) to open and edit this project to custom for more personalized then recompile to take your owner client and server binary files.

All releases [here](https://github.com/sontx/chat-socket/releases), they include source code and binary files.
![](https://lh3.googleusercontent.com/-TmXmzSowGZo/VuEMSqw9ICI/AAAAAAAAOHs/xoU8HmEM1EE/s0/Untitled.png)

> **homechat-client.jar** is binary for chat client which you and your friends will use to chat with another and **homechat-server.jar** is binary for chat server is "brain" where all clients will connect to.

## Usage
Ensure in your computer has java runtime first, lowest version is 1.6 to run client or server or both.
First step, you must download binary files [here](https://github.com/sontx/chat-socket/releases) or export from project in Eclipse IDE(or other what you want), so we have two files are **homechat-client.jar** and **homechat-server.jar**

**homechat-server.jar** is an application server so you have to run in a computer which allow other computers connect to like a server. This is a screenshot about main interface of server, so friendly! You just provide some info such as server ip, server port and a directory where some data will be saved.

![](https://lh3.googleusercontent.com/-66VUBQH7n8A/VuERDlbNAyI/AAAAAAAAOIE/nvLSCx99EyU/s0/Untitled.png)

You can get available ip addresses in your computer by click to **...** button then select an ip address in dialog.

![](https://lh3.googleusercontent.com/-4bf0C57mHec/VuESBedpFXI/AAAAAAAAOIY/NIou4UpCSSQ/s0/Untitled.png)

After you provided all info in fields then click to **Start** button to start server, you will see all log from text area like this:

![](https://lh3.googleusercontent.com/-iq-XDux_9YM/VuES7yHFsjI/AAAAAAAAOIs/GAirp3ufJfU/s0/Untitled.png)

All works of server are done! then we start with client.
Start client by click to **homechat-client.jar**, first screen require you enter server ip and server port which you provide for server from above step.

![](https://lh3.googleusercontent.com/-klz1GyyDC4M/VuETm6TTZzI/AAAAAAAAOJA/mLFzAGnTOpU/s0/Untitled.png)

Then you just click to **Connect** button to start connect to server, after that, a login window appear like this:

![](https://lh3.googleusercontent.com/-mfWUJjvxxPE/VuET-8bnmkI/AAAAAAAAOJQ/G62gq_q-BgQ/s0/Untitled.png)

Ohhhh! you must have an account to login to system. To take the account you just click to **Register** button then provide some info to create new account in server.

![](https://lh3.googleusercontent.com/-m-obEe5l9zk/VuEUjO6IaWI/AAAAAAAAOJk/BZ0RZXTiI4w/s0/Untitled.png)

Finally, enter username and password which you just registered in Register window to login to system. After you logged to system, a main window appear which display all accounts in server so you can chat with them by click to online account which have avatar is ![](https://lh3.googleusercontent.com/-16CnaM1lc9w/VuEWwzjwSWI/AAAAAAAAOKA/q9bxGfxe8NE/s0/online.png), main window like this:

![](https://lh3.googleusercontent.com/-ZeCZ38_xwis/VuEW8Hxz-1I/AAAAAAAAOKQ/SvUGmmssyqw/s0/Untitled.png)

A chat window is very simple with chat box which you enter your chat message and a chat log above to display conversation. Your chat message will be ***red*** and your friend is ***blue***

![](https://lh3.googleusercontent.com/-7n1YOnHZITw/VuEX5_HUExI/AAAAAAAAOKo/g7gDEGF0nZY/s0/Untitled.png)

When you has been received an message from your friend then chat window auto show to display your conversation.
You can change some profile info such as display name, status and blablalba by click your avatar(or your status message) in main window.

![](https://lh3.googleusercontent.com/-emyvdBEDIUg/VuEYuLwa_MI/AAAAAAAAOK8/_rF1-Y9KqKA/s0/Untitled.png)

Your profile window appear to show your info like this:

![](https://lh3.googleusercontent.com/-zhFR0VQa8go/VuEZAETh4rI/AAAAAAAAOLM/xLUMSt6Pnsk/s0/Untitled.png)

You can change password, display name and status from there.
To change password, click to **Change...** link then provide new password in appear dialog.
To change display name or status just enter new info then click **Change...** link to update to server. You change such as display name or status will be auto updated from other clients.
## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

##Dependencies
This project do not depend on platform, they just require java runtime 1.6 or higher so you can run in Windows or Linux or other flatform.
In this project using some libraries:

1. [**common-lang3**](https://commons.apache.org/proper/commons-lang/) to escape html.
2. [**java-json**](http://www.java2s.com/Code/Jar/j/Downloadjavajsonjar.htm) to work with JSON
3. [**jdk 1.7**](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) for develop

## History
This is first version [v1.0.0](https://github.com/sontx/chat-socket/releases), it's not stable version.
## Author
Developed by sontx/noem, some useful information:

 - Home: [www.sontx.in](http://www.sontx.in)
 - My blog: [www.blog.sontx.in](http://www.blog.sontx.in)
 - Email: <a href="mailto:xuanson33bk@gmail.com">xuanson33bk@gmail.com</a>
 - Facebook: [No Em](https://mobile.facebook.com/Melkior.9x)

## License
Copyright 2016 Tran Xuan Son
