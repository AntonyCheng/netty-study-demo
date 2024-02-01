### Netty 缓冲区工具类

Unpooled 是 Netty 提供的一个专门用来操作缓冲区(即 Netty 的数据容器)的工具类，再 Netty 中缓冲区是 ByteBuf 类。

* 常用方法如下：

  * public static ByteBuf copiedBuffer(CharSequence string, Charset charset)，通过给定的数据和字符编码返回一个 ByteBuf 对象（类似于 NIO 中的 ByteBuffer 但有区别）。

* 接下来用示例代码说明 Unpooled 获取 Netty 数据容器 ByteBuf 的基本使用方法，下面是 ByteBuf 的一个结构示意简图：

  ![img.png](assets/img.png)

  * 使用 buffer() 或者 directBuffer() 创建一个容量为 capacity 的缓冲区，初始化状态 readerIndex 和 writerIndex 均为0。

  * 当写入一个数据时 writerIndex 会加1，读取一个数据时 readerIndex 会加1。

  * 已读区域为 0 ~ readerIndex，可读区域为 readerIndex ~ writerIndex，可写区域为 writerIndex ~ capacity，如果有效数据存放在缓冲区中，那么其位置一定是在可读区域。

  * 如果是使用 copiedBuffer，此时 capacity 的值就不再是开发者指定内容的长度，而是通过一种算法计算出来的，真实的可读区域最大值是 writerIndex，所以建议无论那种创建缓冲区的方法，需要循环读取缓冲区中的数据时，可读最大索引均使用 writerIndex 或者使用 readableBytes() 方法。