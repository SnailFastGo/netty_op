package com.serialize.msgpack;

import java.util.HashSet;
import java.util.Set;

import org.msgpack.annotation.Message;

@Message
public class Student {
    
    private int id;
    
    private String name;
    
    private String address;
    
    private Set<String> hobbies = new HashSet<>();

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
}
