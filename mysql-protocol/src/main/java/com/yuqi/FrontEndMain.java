package com.yuqi;

import com.yuqi.protocol.ProtocolMainThread;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 30/6/20 22:25
 **/
public class FrontEndMain {
    public static void main(String[] args) {
        int port = 3016;

        LifeCycleInstance.start();

        //start netty port
        new Thread(new ProtocolMainThread(port)).start();
    }
}
