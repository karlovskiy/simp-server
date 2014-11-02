# SIMP server 

SIMP server is the lightweight blocking I/O server for SIMP messaging protocol.

## Simple Message Protocol (SIMP)

SIMP is an lightweight binary protocol for chat messages.

#### *Connect* and *Disconnect*

This request types can be used to connect to the chat server or disconnection from the chat server.

```
--------------------------------------------
| Protocol | Request | Username |          |
| Version  | Type    | length   | Username |
| 1 byte   | 1 byte  | 1 byte   |          |
--------------------------------------------
```

#### *Message*

This type of request can be used to send messages to the chat server.

```
----------------------------------------------------------------
| Protocol | Request | Username |          | Message |         |
| Version  | Type    | length   | Username | length  | Message |
| 1 byte   | 1 byte  | 1 byte   |          | 2 bytes |         |
----------------------------------------------------------------
```

#### *Connect successfully*

This is a response from the server after successfully connection established.

```
-------------------------------------------
| Protocol | Response | Users   | Users   |
| Version  | Type     | length  | online  |
| 1 byte   | 1 byte   | 2 bytes | current |
-------------------------------------------
```

#### *User connected* and *User disconnected*

This response types from the server aftre another user connected or disconnected.

```
---------------------------------------------
| Protocol | Response | Username |          |
| Version  | Type     | length   | Username |
| 1 byte   | 1 byte   | 1 byte   |          |
---------------------------------------------
```

#### *Message*

This response from the server on another users messages.

```
-----------------------------------------------------------------
| Protocol | Response | Username |          | Message |         |
| Version  | Type     | length   | Username | length  | Message |
| 1 byte   | 1 byte   | 1 byte   |          | 2 bytes |         |
-----------------------------------------------------------------
```


