package top.sharehome.rpc.provider.impl;

import top.sharehome.rpc.common.HelloService;

/**
 * HelloService实现类
 *
 * @author AntonyCheng
 */

public class HelloServiceImpl implements HelloService {
    /**
     * 当有消费方调用该方法时就返回一个结果
     */
    @Override
    public String hello(String message) {
        // 根据message返回不同的结果
        if (message != null && !message.isEmpty()) {
            return "你好，客户端！我已经收到了你的消息：" + message;
        } else {
            return "你好，客户端！你的消息为空！";
        }
    }
}
