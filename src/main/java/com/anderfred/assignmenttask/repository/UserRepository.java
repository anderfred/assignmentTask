package com.anderfred.assignmenttask.repository;

import com.anderfred.assignmenttask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
