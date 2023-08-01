package com.cafe.dao;

import com.cafe.model.User;
import com.cafe.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByEmailId(@Param("email") String email);

    List<UserWrapper> getAllUser();

    List<String> getAllAdmin();

    // whenever we use update query use this two annotations
    @Transactional
    @Modifying
    Integer updateUserStatus(@Param("status") String status, @Param("id") Integer id);

    User findByEmail(String email);
}
