### NIO中的Selector

Selector一般称为选择器，也可以被称为多路复用器，用于检查一个或多个NIO Channel（通道）的状态。

但不是所有的Channel都可以被Selector复用，例如FileChannel就不能。判断一个Channel能否被Selector复用，取决于它是否继承了SelectableChannel抽象类。一般来说所有Socket类型通道以及从管道（Pipe）对象中获取的通道都能被Selector复用。

一个通道可以被注册到多个选择器上，但对每个选择器而言只能被注册一次。通道和选择器之间的关系，使用注册的方式完成。SelectableChannel可以被注册到Selector对象上，在注册的时候，需要指定通道的哪些操作的就绪状态是Selector感兴趣的：

* 可读：SelectionKey.OP_READ
* 可写：SelectionKey.OP_WRITE
* 连接：SelectionKey.OP_CONNECT
* 接收：SelectionKey.OP_ACCEPT

如果Selector对于通道的多操作类型感兴趣，可以使用“位活”操作符来实现：

```java
int key = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
```

SelectorKey说明：

1. Channel注册后，一旦通道处于注册时要求的就绪状态，就可以被选择器查询到，使用Selector的select()方法就能完成。
2. Selector可以不断查询Channel中操作的就绪状态，一旦有Selector感兴趣的操作，就会被放入SelectionKey集合中。
3. 一个SelectionKey中包含了被选择通道的操作类型，也包含了Channel和Selector之间的注册关系。在整个NIO体系中选择键是编程的关键，其就是根据对应的选择键进行不同的业务逻辑的处理。
4. 选择键的概念和事件（Event）的概念比较相似，一个选择键类似监听器模式中的一个事件，但是Selector并不是事件触发，而是主动查询。

最开始已经说道，Selector检查的是某个操作的就绪状态，而不是操作本身。

这里的就绪状态就是下一步将要执行操作的状态，就比如下一步就是写操作，那就是可写就绪状态，以此类推。