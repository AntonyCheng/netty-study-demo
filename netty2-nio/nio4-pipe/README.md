### NIO中的Pipe

Pipe一般称为管道，用于两个线程之间的单向数据连接，Pipe有一个source通道和一个sink通道

```text
         ____________________________________
         |              (Pipe)              |
ThreadA--|-->Sink Channel-->Source Channel--|-->ThreadB
         |__________________________________|
```