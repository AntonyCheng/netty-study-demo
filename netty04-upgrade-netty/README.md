## Netty的进阶使用和深入理解

经过 Netty 基础的学习，在实际开发中其实就已经足够用了，接下来主要说明一些 Netty 更底层的设计和遇到的一些比较特殊的问题，例如粘包、半包。

**内容如下**：

1、Netty 编解码器和 Handler 的调用机制 ==> **netty11-codec-handler**

2、Netty 整合 Log4j2 ==> **netty12-log4f2**

3、TCP 粘包和半包以及解决方案 ==> **netty13-coalescing-splitting**

4、Netty 核心源码剖析 ==> **netty14-core-source**

5、用 Netty 实现 dubbo RPC ==> **netty15-rpc**