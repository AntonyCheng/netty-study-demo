## Netty的进阶使用和深入理解

经过 Netty 基础的学习，在实际开发中其实就已经足够用了，接下来主要说明一些 Netty 更底层的设计和遇到的一些及其特殊的问题，例如粘包、半包。

**内容如下**：

1、Netty 编解码器和 Handler 的调用机制 ==> **netty11-codec-handler**

2、TCP 粘包和半包以及解决方案 ==> **netty12-coalescing-splitting**

3、Netty 核心源码剖析 ==> **netty13-core-source**

4、用 Netty 实现 dubbo RPC ==> **netty14-rpc**