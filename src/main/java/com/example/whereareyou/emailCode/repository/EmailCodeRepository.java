package com.example.whereareyou.emailCode.repository;

import com.example.whereareyou.emailCode.domain.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCode,String> {
    EmailCode findByEmail(String email);
}
