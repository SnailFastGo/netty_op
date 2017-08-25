package com.serialize.fastjson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Student{
    
    private int id;
    
    private String name;
    
    private String address;
    
    private Set<String> hobbies = new HashSet<>();
    
    private Map<String, String> others = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Set<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<String> hobbies) {
        this.hobbies = hobbies;
    }

    public void addHobby(String hobby){
        this.hobbies.add(hobby);
    }
    
    public boolean removeHobby(String hobby){
        boolean remove = this.hobbies.remove(hobby);
        return remove;
    }
    
    public void put(String key, String value){
        this.others.put(key, value);
    }
    
    public String get(String key){
        String value = this.others.get(key);
        return value;
    }

    public Map<String, String> getOthers() {
        return others;
    }

    public void setOthers(Map<String, String> others) {
        this.others = others;
    }
}
