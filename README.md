# SIMP server 

SIMP server is the lightweight blocking I/O server for SIMP messaging protocol.

## Simple Message Protocol (SIMP)

SIMP is an lightweight binary protocol for chat messages.

#### *Connect request* 

This request types can be used to connect to the chat server.

```
---------------------------------------------
| Protocol | Request  | Username |          |
| Version  | Type     | length   | Username |
| 1 byte   | 1 byte   | 1 byte   |          |
| 00000001 | 00000000 |          |          |
---------------------------------------------
```

#### *Disconnect request*

This request types can be used to disconnection from the chat server.

```
---------------------------------------------
| Protocol | Request  | Username |          |
| Version  | Type     | length   | Username |
| 1 byte   | 1 byte   | 1 byte   |          |
| 00000001 | 00000001 |          |          |
---------------------------------------------
```

#### *Message request*

This type of request can be used to send messages to the chat server.

```
-----------------------------------------------------------------
| Protocol | Request  | Username |          | Message |         |
| Version  | Type     | length   | Username | length  | Message |
| 1 byte   | 1 byte   | 1 byte   |          | 2 bytes |         |
| 00000001 | 00000010 |          |          |         |         |
-----------------------------------------------------------------
```

#### *Connect successfully response*

This is a response from the server after successfully connection established.

```
-------------------------------------------
| Protocol | Response | Users   | Users   |
| Version  | Type     | length  | online  |
| 1 byte   | 1 byte   | 2 bytes | current |
| 00000001 | 00000001 |         |         |
-------------------------------------------
```

#### *User connected response*

This response types from the server after another user connected.

```
---------------------------------------------
| Protocol | Response | Username |          |
| Version  | Type     | length   | Username |
| 1 byte   | 1 byte   | 1 byte   |          |
| 00000001 | 00000010 |         |         |
---------------------------------------------
```

#### *User disconnected response*

This response types from the server after another user disconnected.

```
---------------------------------------------
| Protocol | Response | Username |          |
| Version  | Type     | length   | Username |
| 1 byte   | 1 byte   | 1 byte   |          |
| 00000001 | 00000011 |          |          |
---------------------------------------------
```

#### *Message response*

This response from the server on another users messages.

```
-----------------------------------------------------------------
| Protocol | Response | Username |          | Message |         |
| Version  | Type     | length   | Username | length  | Message |
| 1 byte   | 1 byte   | 1 byte   |          | 4 bytes |         |
| 00000001 | 00000100 |          |          |         |         |
-----------------------------------------------------------------
```

#### *Request type*
Type | Dec | Binary
--- | :---: | :---:
CONNECT | 0 | 00000000
DISCONNECT | 1 | 00000001
MESSAGE | 2 | 00000010

#### *Response type*
Type | Dec | Binary
--- | :---: | :---:
ERROR | 0 | 00000000
CONNECT_SUCCESSFULLY | 1 | 00000001
USER_CONNECTED | 2 | 00000010
USER_DISCONNECTED | 3 | 00000011
MESSAGE | 4 | 00000100
