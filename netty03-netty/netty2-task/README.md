### Netty Task（任务）的使用

在Netty中，NioEventLoop可以看作是最小的工作单元，该单元中包含了一个Selector、一个TaskQueue以及一个ScheduleTaskQueue。

TaskQueue存放的是异步任务（按照编码顺序直接放进异步队列），ScheduleTaskQueue存放的是延时异步任务（一定时间之后放进异步队列）。

Netty底层就是NIO，但是在实际业务当中，比如遇到计算量极大（夸张点：复杂度n^n次方）的任务时，NioEventLoop肯定会形成阻塞。

此时TaskQueue和ScheduleTaskQueue就能发挥作用，使该计算业务在消息队列中排队，然后异步执行。

**注意**： 当TaskQueue和ScheduleTaskQueue的任务同时存在时，依然按照它们的特点进入同一个异步队列，进入队列的顺序就是执行顺序，现在举一些例子：

1. 编码顺序如下：1、异步任务A耗时2s； 2、延时任务B延时5s，耗时2s； 3、异步任务C耗时2s。此时进入队列的顺序为 A(0s) -> C(0s) -> B(5s) ，那么执行效果如下：
    * 2s时 ==> A完成
    * 4s时 ==> C完成
    * 7s时 ==> B完成
2. 编码顺序如下：1、异步任务A耗时2s； 2、延时任务B延时1s，耗时0s； 3、异步任务C耗时2s。此时进入队列的顺序为 A(0s) -> C(0s) -> B(1s) ，那么执行效果如下：
    * 2s时 ==> A完成
    * 4s时 ==> C完成
    * 4s时 ==> B完成
