package com.yuqi.schema.common.bean;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/3/20 22:16
 **/
public class ClassInitTest {
    private static int num1 = 30;

    private static int num2 = 20;  //num2在准备阶段就被设置了默认初始值0，初始化阶段又将10改为20

    static {
        num1 = 10;
        num2 = 10;     //num2写在定义变量之前，为什么不会报错呢？？
        System.out.println(num2);   //這裡直接打印可以吗？报错，非法的前向引用，可以赋值，但不可调用
    }
    public static void main(String[] args) {
        System.out.println(num1);  //10
        System.out.println(num2);   //20
    }
}
