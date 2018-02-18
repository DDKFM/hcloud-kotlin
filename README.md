# hcloud-kotlin
A kotlin api for the hetzner cloud api.

The Hetzner Cloud is a REST-API for managing their Hetzner-Cloud product. 
The API needs a valid token which can be generated on the webinterface from Hetzner:
https://console.hetzner.cloud/

**Getting Started**
```java
val token : String = "..."
var hCloud = KoHCloud(token = token);
val servers = hCloud.getServers();
/*
* Show all servernames
* */
for(server in servers) {
    println(server.name)
}
```


