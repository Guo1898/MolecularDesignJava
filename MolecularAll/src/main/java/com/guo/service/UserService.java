package com.guo.service;


import com.guo.mapper.UserMapper;
import com.guo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserMapper userMapper;


    public void registerUser(String username, String password){
        userMapper.register(username, password);
    }

    public User login(String username, String password) {
        return userMapper.findByUsernameAndPassword(username, password);
    }

    public void UpdateNewFolder(String username, String newFolder){
        userMapper.updateNewFolder(username,newFolder);
    }

    public String QueryNewfolder(String username){
        return userMapper.queryNewFolder(username);
    }

    public User FindByUsername(String username){
        return userMapper.FindByName(username);
    }
}