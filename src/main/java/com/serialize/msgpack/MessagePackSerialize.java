package com.serialize.msgpack;

import java.io.IOException;

import org.msgpack.MessagePack;

/**
 * @project 使用MessagePack进行序列化和反序列化
 * @file MessagePackSerialize.java 创建时间:2017年8月10日下午12:52:00
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class MessagePackSerialize {
    private static final MessagePack messagePack = new MessagePack();
    
    /**
     *@description 将对象序列化成字节数组
     *@time 创建时间:2017年8月2日下午4:47:05
     *@param o 需要序列化的对象
     *@return
     *@throws IOException
     *@author dzn
     */
    public static byte[] ObjectToBytes(Object o) throws IOException{
        if(null == o){
            return null;
        }
        byte[] serialBytes = messagePack.write(o);
        return serialBytes;
    }
    
    /**
     *@description 将字节数组反序列化成字节对象
     *@time 创建时间:2017年8月2日下午4:47:18
     *@param bytes 字节数组
     *@param c 目标Class
     *@return
     *@throws IOException
     *@author dzn
     */
    public static <T> T bytesToObject(byte[] bytes, Class<T> c) throws IOException{
        if(null == bytes || bytes.length == 0){
            return null;
        }
        T res = messagePack.read(bytes, c);
        return res;
    }
    
    public static void main(String[] args) throws IOException {
        Student student = new Student();
        student.setId(123);
        student.setName("小明");
        student.setAddress("北京三里屯");
        student.addHobby("basketball");
        student.addHobby("football");
        student.addHobby("skiing");
        long start = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i ++){
            byte[] bytes = MessagePackSerialize.ObjectToBytes(student);
        }
        long end = System.currentTimeMillis();
//        Student newStudent = MessagePackSerialize.bytesToObject(bytes, Student.class);
        System.out.println(end - start);
//        System.out.println(newStudent.getId());
//        System.out.println(newStudent.getName());
//        System.out.println(newStudent.getAddress());
//        System.out.println(newStudent.getHobbies());
    }
}
