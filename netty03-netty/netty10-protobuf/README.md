### Google Protobuf

Protobuf 是 Google 发布的一种结构化数据存储格式，适合做序列化，在此之前需要对编码/解码有一定了解。

#### 编码/解码

编写网络应用程序时，因为数据在网络中传输的都是二进制字节码数据，在发送数据时就需要编码，接收数据时就需要解码。

codec(编解码器) 的组成部分有两个：decoder(解码器) 和 encoder(编码器)。encoder 负责把业务数据转换成字节码数据，decoder 负责把字节码数据转换成业务数据。

举个例子：Java程序的业务数据被编码器编码之后在网络中以二进制字节码的形式进行传输，Python程序的解码器获取到二进制字节码之后解码为属于自己的业务数据。

**Netty 本身存在的编码/解码机制**：

1. Netty 提供的一些 Codec (编解码器)

   * HttpServerCodec Http协议数据编解码器

2. Netty 提供的一些 Encoder (编码器)

   * StringEncoder 对字符串数据进行编码
   
   * ObjectEncoder 对 Java 对象进行编码

3. Netty 提供的一些 Decoder (解码器)
   
   * StringDecoder 对字符串数据进行解码
   
   * ObjectDecoder 对 Java 对象进行解码
   
4. Netty 本身自带的 ObjectDecoder 和 ObjectEncoder 可以用来实现 POJO 对象或各种业务对象的编码和解码，底层使用的仍是 Java 序列化技术 , 而 Java 序列化技术本身效率就不高，存在如下问题：
   
   * 无法跨语言。
   
   * 序列化后的体积太大，是二进制编码的 5 倍多。
   
   * 序列化性能太低。

#### Protobuf

根据上述的问题，就能引出 Protobuf ，Protobuf 是 Google 发布的开源项目，全称 Google Protocol Buffers，是一种轻便高效的结构化数据存储格式，可以用于结构化数据串行化，或者说序列化。它很适合做数据存储或 RPC[远程过程调用 remote procedure call] 数据交换格式。

目前大多数公司、机构或者框架都在使用 TCP + Protobuf 的数据序列化方案，而不是过去的 HTTP + JSON 的方案。

官方参考文档链接如下：https://protobuf.dev/overview/

**Protobuf 的数据类型和其他语言类型转换一览：**

| .proto Type | Notes（注释）                                | C++ Type | Java/Kotlin Type | Python Type                     | Go Type | Ruby Type            | C# Type    | Dart Type |
|-------------|------------------------------------------|----------|------------------|---------------------------------|---------|----------------------|------------|-----------|
| double      |                                          | double   | float            | *float64                        | Float   | double               | double     |
| float       |                                          | float    | float            | *float32                        | Float   | float                | double     |
| int32       | 使用可变长度编码。对于编码负数效率低，如果字段可能包含负值，请改用sint32。 | int32    | int              | int                             | int32   | Fixnum或Bignum（根据需要）  | int        | *int32    |
| int64       | 使用可变长度编码。对于编码负数效率低，如果字段可能包含负值，请改用sint64。 | int64    | long             | int/long                        | *int64  | Bignum               | long       | Int64     |
| uint32      | 使用可变长度编码。                                | uint32   | int              | int/long                        | *uint32 | Fixnum或Bignum（根据需要）  | uint       | int       |
| uint64      | 使用可变长度编码。                                | uint64   | long             | int/long                        | *uint64 | Bignum               | ulong      | Int64     |
| sint32      | 使用可变长度编码。有符号整数值。相对于常规int32，这些更有效地编码负数。   | int32    | int              | int                             | int32   | Fixnum或Bignum（根据需要）  | int        | *int32    |
| sint64      | 使用可变长度编码。有符号整数值。相对于常规int64，这些更有效地编码负数。   | int64    | long             | int/long                        | *int64  | Bignum               | long       | Int64     |
| fixed32     | 始终为四个字节。如果值通常大于2^28，则比uint32更有效。         | uint32   | int              | int/long                        | *uint32 | Fixnum或Bignum（根据需要）  | uint       | int       |
| fixed64     | 始终为八个字节。如果值通常大于2^56，则比uint64更有效。         | uint64   | long             | int/long                        | *uint64 | Bignum               | ulong      | Int64     |
| sfixed32    | 始终为四个字节。                                 | int32    | int              | int                             | *int32  | Fixnum或Bignum（根据需要）  | int        | int       |
| sfixed64    | 始终为八个字节。                                 | int64    | long             | int/long                        | *int64  | Bignum               | long       | Int64     |
| bool        |                                          | bool     | boolean          | bool                            | *bool   | TrueClass/FalseClass | bool       | bool      |
| string      | 字符串必须始终包含UTF-8编码或7位ASCII文本，并且长度不能超过2^32。 | string   | String           | unicode（Python 2）或str（Python 3） | *string | String（UTF-8）        | string     | String    |
| bytes       | 可包含不超过2^32个字节的任意字节序列。                    | string   | ByteString       | bytes                           | []byte  | String（ASCII-8BIT）   | ByteString | List      |


**Protobuf 特点如下**：

* Protobuf 是以 Message 的方式来管理数据的。

* 支持跨平台、跨语言，即[客户端和服务器端可以是不同的语言编写的]（支持目前绝大多数语言，例如 C++ 、 C# 、 Java 、 Python 等）。

* 高性能，高可靠性。

* 使用 protobuf 编译器能自动生成代码， Protobuf 是将类的定义使用 .proto 文件进行描述说明，在 IDEA 中编写 .proto 文件时，会自动提示是否下载 .proto 编写插件. 可以让语法高亮。然后通过 protoc.exe 编译器根据 .proto 自动生成 .java 文件。该 .java 文件通过 ProtobufEncoder 编码之后以二进制字节码进行传输，到达数据接收端之后使用 ProtobufDecoder 进行解码再使用，这里要注意生成和传输的 .java 文件中有一个内部类，而这个内部类才是开发者实际的业务类。

**入门代码示例**：

1. 单数据类型示例：

   * 客户端可以发送一个 Student POJO 对象到服务器 (通过 Protobuf 编码)。

   * 服务端能接收Student POJO 对象，并显示信息(通过 Protobuf 解码)。

2. 多数据类型示例：

   * 客户端可以随机发送 Boss POJO / Worker POJO 对象到服务器 (通过 Protobuf 编码)。
   
   * 服务端能接收 Boss POJO / Worker POJO 对象(需要判断是哪种类型)，并显示信息(通过 Protobuf 解码)