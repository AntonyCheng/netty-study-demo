### Netty Future（异步模型）

在 `netty2-task` 模块中已经了解到如何将业务层面的代码也进行异步化操作，那么现在就出现了一个问题：如何监听异步操作的结果呢？接下来 Future-Listener 机制会给出答案。

#### 基本介绍

1. 异步的概念和同步相对。当一个异步过程调用发出后，调用者不能立刻得到结果。实际处理这个调用的组件在完成后，通过状态、通知和回调来通知调用者。

2. Netty 中的 I/O 操作是异步的，包括 Bind、Write、Connect 等操作会简单的返回一个ChannelFuture。

3. 调用者并不能立刻获得结果，而是通过 Future-Listener 机制，用户可以方便的主动获取或者通过通知机制获得 IO 操作结果

4. Netty 的异步模型是建立在 future 和 callback 的之上的。callback 就是回调。重点说 Future，它的核心思想是：假设一个方法 fun，计算过程可能非常耗时，等待 fun 返回显然不合适。那么可以在调用 fun 的时候，立马返回一个 Future，后续可以通过 Future去监控方法 fun 的处理过程(即：Future-Listener 机制)。

#### Future说明

1. 表示异步的执行结果, 可以通过它提供的方法来检测执行是否完成，比如检索计算等。

2. ChannelFuture 是一个接口 ： public interface ChannelFuture extends Future<Void> 我们可以添加监听器，当监听的事件发生时，就会通知到监听器。

#### 工作原理示意图

![img.png](assets/img.png)

**说明如下**：

1. 在使用 Netty 进行编程时，拦截操作和转换出入站数据只需要您提供 callback 或利用 future 即可。这使得链式操作简单、高效, 并有利于编写可重用的、通用的代码。

2. Netty 框架的目标就是让你的业务逻辑从网络基础应用编码中分离出来、解脱出来。