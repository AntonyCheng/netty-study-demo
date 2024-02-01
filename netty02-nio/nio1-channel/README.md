### NIO中主要的Channel

* FileChannel 处理文件的管道

* SocketChannel 处理TCP连接的管道

* ServerSocketChannel 监听传入的连接和新建的SocketChannel对象

* DatagramChannel 处理UDP连接的管道

**针对Socket通道的注意事项**：

1. 后三种统称为Socket通道，这些通道都继承了一个AbstractSelectableChannel抽象类，这就意味着可以使用一个Selector对象来执行Socket通道的就绪选择。

2. Socket通道中实现定义读和写功能的接口只有SocketChannel和DatagramChannel，意味着传输数据是由这两类通道完成的。

3. Socket通道既可以在阻塞模式下工作，也可以在非阻塞模式下工作，模式变换取决于configureBlocking(Boolean)方法中传入的是Ture还是False。