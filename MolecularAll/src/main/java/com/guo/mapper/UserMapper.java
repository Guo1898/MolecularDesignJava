package com.guo.mapper;

import com.guo.pojo.Trait;
import com.guo.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into moleculardesign.users (username, password) values(#{username}, #{password})")
    int register(@Param("username") String username, @Param("password") String password);


    @Select("select * from moleculardesign.users WHERE username = #{username} AND password = #{password}")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);


    @Select("select * from moleculardesign.users WHERE username = #{username}")
    User FindByName(@Param("username") String username);


    @Update("UPDATE moleculardesign.users SET newFolder = #{newFolder} WHERE username = #{username}")
    int updateNewFolder(@Param("username") String username, @Param("newFolder") String newFolder);

    @Select("SELECT newFolder FROM moleculardesign.users WHERE username = #{username}")
    String queryNewFolder(@Param("username") String username);




}
