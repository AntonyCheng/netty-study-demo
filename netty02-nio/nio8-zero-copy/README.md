### NIO与零拷贝

有关零拷贝，其实经历了一些发展阶段，数据流向如下：

1. 传统的IO模型：**Hard drive** ==DMA copy==> **kernel buffer** ==CPU copy==> **user buffer** ==CPU copy==> **socket buffer** ==DMA copy==> **protocol engine**

2. mmap（内存映射）优化：**Hard drive** ==DMA copy==> **kernel buffer** ==CPU copy==> **socket buffer** ==DMA copy==> **protocol engine**

3. sendFile（Linux 2.1版本）优化：**Hard drive** ==DMA copy==> **kernel buffer** ==CPU copy==> **socket buffer** ==DMA copy==> **protocol engine**

4. sendFile（Linux 2.4版本）优化：**Hard drive** ==DMA copy==> **kernel buffer** ==DMA copy==> **protocol engine**

注意：
1. sendFile（Linux 2.4版本）优化中真实情况依旧还是有一次从**kernel buffer** ==CPU copy==> **socket buffer**的过程，但是拷贝的信息极少，比如length，offset，消耗低到可以忽略。

2. mmap适合小数据量读写，sendFile适合大文件传输。

所以结合发展理解一下零拷贝：

* 零拷贝是针对于**操作系统的角度**说的，因为内核缓冲区之间，没有数据是重复的（只有kernel buffer有一份数据）。

* 零拷贝不仅仅带来更少的数据复制，还能带来其他的性能优势，比如更少的上下文切换、更少的CPU缓存伪共享以及CPU校验和计算。
