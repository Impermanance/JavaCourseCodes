package com.java.jvm.homework;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class XlassLoader extends ClassLoader {
    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new XlassLoader();
        Class<?> helloClass = classLoader.loadClass( "Hello");
        try {
            Object obj = helloClass.newInstance();
            Method method = helloClass.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Hello.xlass");
        try {
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            byte[] classBytes = decode(byteArray);
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            close(inputStream);
        }
    }

    /**
     * y=255-x => x=255-y
     * @param byteArray 字节数组
     * @return
     */
    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }

    /**
     * 关闭流
     * @param inputStream 输入流
     */
    private static void close(Closeable inputStream) {
        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
