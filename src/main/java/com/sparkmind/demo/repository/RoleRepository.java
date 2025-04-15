package com.sparkmind.demo.repository;

import com.sparkmind.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Tìm kiếm vai trò dựa trên tên vai trò
    Optional<Role> findByRoleName(String roleName);

    // Kiểm tra xem vai trò đã tồn tại hay chưa
    boolean existsByRoleName(String roleName);
    
}
