package com.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @project 使用Fastjsob进行序列化和反序列化
 * @file FastjsonSerialize.java 创建时间:2017年8月25日下午4:53:09
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class FastjsonSerialize {
    /**
     *@description 将对象序列化成字节数组
     *@time 创建时间:2017年8月10日下午12:41:16
     *@param o
     *@return
     *@author dzn
     */
    public static byte[] objectToBytes(Object o){
        String str = objectToString(o);
        if(str == o){
            return null;
        }else{
            byte[] bytes = JSON.toJSONBytes(str, SerializerFeature.PrettyFormat);
            return bytes;
        }
    }
    
    /**
     *@description 将字节数组反序列化成字节对象
     *@time 创建时间:2017年8月10日下午12:41:41
     *@param bytes
     *@return
     *@author dzn
     */
    public static <T> T bytesToObject(byte[] bytes, Class<T> c){
        if(null == bytes || bytes.length <= 0){
            return null;
        }else{
            String str = (String)JSON.parse(bytes, Feature.SupportArrayToBean);
            T t = stringToObject(str, c);
            return t;
        }
    }
    
    /**
     *@description 将Java bean转换成JSON字符串
     *@time 创建时间:2017年8月25日下午2:55:55
     *@param o
     *@return
     *@author dzn
     */
    public static String objectToString(Object o){
        if(null == o){
            return null;
        }else{
            String jsonString = JSON.toJSONString(o);
            return jsonString;
        }
    }
    
    /**
     *@description 将字符串转换成Java bean
     *@time 创建时间:2017年8月25日下午3:26:52
     *@param str
     *@param c
     *@return
     *@author dzn
     */
    @SuppressWarnings("unchecked")
    public static <T> T stringToObject(String str, Class<T> c){
        if(null == str){
            return null;
        }else{
            Object o = JSON.parseObject(str, c);
            return (T)o;
        }
    }
    
    public static void main(String[] args) {
        Student student = new Student();
        student.setId(123);
        student.setAddress("北辰世纪中心A座");
        student.setName("张晓明");
        student.addHobby("basketball");
        student.addHobby("football");
        student.put("abc", "123");
        student.put("qwe", "987");
//        String str = objectToString(student);
//        System.out.println(str);
//        Student newStudent = stringToObject(str, Student.class);
        byte[] bytes = objectToBytes(student);
        Student newStudent = bytesToObject(bytes, Student.class);
        System.out.println("id :" + newStudent.getId());
        System.out.println("name :" + newStudent.getName());
        System.out.println("address :" + newStudent.getAddress());
        System.out.println("hobby :" + newStudent.getHobbies());
        System.out.println("other :" + newStudent.getOthers());
    }
}
