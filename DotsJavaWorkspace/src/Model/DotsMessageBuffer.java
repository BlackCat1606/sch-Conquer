//package Model;
//
//import java.util.concurrent.LinkedBlockingQueue;
//
///**
// * Created by JiaHao on 16/3/15.
// */
//public class DotsMessageBuffer {
//
//    private final LinkedBlockingQueue<String> queue;
//    private final DotsLocks locks;
//
//    public DotsMessageBuffer(DotsLocks locks) {
//        this.queue = new LinkedBlockingQueue<String>();
//        this.locks = locks;
//    }
//
//
//    public void push(String message) throws InterruptedException {
//
//
//        queue.put(message);
//
//    }
//
//
//}
