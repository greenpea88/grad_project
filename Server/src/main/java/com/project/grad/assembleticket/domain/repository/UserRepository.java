package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    int countByEmail(String email);

}
