package com.serialize.jdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @project 使用JDK原生的方式进行序列化和反序列化
 * @file JDKSerialize.java 创建时间:2017年8月10日下午12:52:31
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class JDKSerialize {
    
    /**
     *@description 将对象序列化成字节数组
     *@time 创建时间:2017年8月10日下午12:41:16
     *@param o
     *@return
     *@author dzn
     */
    public static byte[] ObjectToBytes(Object o){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        byte[] bytes = null;
        try {
            oos = new ObjectOutputStream(os);
            oos.writeObject(o);
            bytes = os.toByteArray();
            oos.close();
            os.close();
        } catch (Exception e) {
            System.out.println("序列化失败");
        }
        return bytes;
    }
    
    /**
     *@description 将字节数组反序列化成字节对象
     *@time 创建时间:2017年8月10日下午12:41:41
     *@param bytes
     *@return
     *@author dzn
     */
    public static Object bytesToObject(byte[] bytes){
        if(null == bytes || bytes.length <= 0){
            return null;
        }else{
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object res = ois.readObject();
                return res;
            } catch (Exception e) {
                System.out.println("反序列化失败");
            }
            return null;
        }
    }
    
    public static void main(String[] args) {
        Student student = new Student();
        student.setId(123);
        student.setName("小明");
        student.setAddress("北京三里屯");
        student.addHobby("basketball");
        student.addHobby("football");
        student.addHobby("skiing");
        long start = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i ++){
            byte[] bytes = JDKSerialize.ObjectToBytes(student);
        }
        long end = System.currentTimeMillis();
//        JDKSerialize.bytesToObject(bytes);
        System.out.println(end - start);
    }
}
