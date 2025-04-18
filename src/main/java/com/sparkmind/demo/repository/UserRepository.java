package com.sparkmind.demo.repository;

import com.sparkmind.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    // Tìm User dựa trên địa chỉ email
    Optional<User> findByEmail(String email);

    // Kiểm tra xem địa chỉ email đã tồn tại hay chưa
    boolean existsByEmail(String email);

    // Tìm User dựa trên verify token
    Optional<User> findByVerificationToken(String token);

    // Tìm User dựa trên reset pass token
    Optional<User> findByResetPasswordToken(String token);

    //Tìm các User dựa trên role
    List<User> findByRole_RoleName(String roleName);

    //Tìm User dựa trên tên
    List<User> findByFullNameContainingIgnoreCase(String nameKeyword);

    
}
