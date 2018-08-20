package com.mall.dao;

import com.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("username") String username, @Param("md5Password") String md5Password);

    int checkEmail(String email);

    String selectQuestionByUsername(String username);

    String selectAnswerByUsername(@Param("username") String username, @Param("question") String question);

    User selectByUsername(String username);

    int checkAnswer(@Param("usrname") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username, @Param("password") String md5Password);

    int checkPassword(@Param("password") String s, @Param("id") Integer id);

    int checkEmailByUSerId(@Param("email") String email, @Param("id") Integer id);
}