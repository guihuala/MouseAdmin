package com.example.adminapi.mapper;

import com.example.adminapi.entity.User;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;


@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users(username, password, email, role, created_at, avatar) VALUES(#{username}, #{password}, #{email}, #{role}, NOW(), #{avatar})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Update("UPDATE users SET username = #{username}, email = #{email}, role = #{role} ,avatar = #{avatar} WHERE id = #{id}")
    void updateById(User user);

    @Update("UPDATE users SET password = #{password} WHERE username = #{username} AND email = #{email}")
    void updatePassword(@Param("username") String username, @Param("email") String email, @Param("password") String password);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectById(@Param("id") Long id);

    @Select("SELECT * FROM users")
    List<User> selectAll();

    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
}