# A user behaviour collector demo 

1. use netty to build a reverse proxy server.
1. change the backend server response and add a script to load the js detector.
1. js detector loaded by user agent and then collect the user operating information.
1. analyze the user behaviour by these operating information.

*DO NOT SUPPORT HTTPS BACKEND SERVER*

we can not decrypt the message sending through https, so add a script after HttpContent is unrealizable.
