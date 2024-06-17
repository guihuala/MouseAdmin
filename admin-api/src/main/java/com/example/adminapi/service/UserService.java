package com.example.adminapi.service;

import com.example.adminapi.entity.User;
import com.example.adminapi.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void register(User user) {
        userMapper.insert(user);
    }

    public User login(String username, String password) {
        return userMapper.selectByUsernameAndPassword(username, password);
    }

    public void updateUserInfo(Long id, User user) {
        user.setId(id);
        userMapper.updateById(user);
    }

    public void updatePassword(String username, String email, String password) {
        userMapper.updatePassword(username, email, password);
    }

    public User getUserInfo(String username) {
        return userMapper.selectByUsername(username);
    }

    public User isUsernameTaken(String username) {
        return userMapper.selectByUsername(username);
    }

    public User getUserById(long id){
        return userMapper.selectById(id);
    }

    public void deleteUserById(long id){
        userMapper.deleteById(id);
    }
}