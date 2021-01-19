package com.yuqi.scheme.storage;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午6:43
 **/
public class Test {
  public static void main(String[] args) {


    new Thread(() -> {

      int i = 0;
      while (true) {
//        if (i > 5) {
//          break;
//        }
        System.out.println("child thread");

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        i++;
      }
    }).start();


    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
