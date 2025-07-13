package com.loopers.domain.member;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "member")
public class MemberModel extends BaseEntity {

    @Column(name = "login_id", nullable = false, length = 10)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    protected MemberModel() {
    }

    public MemberModel(String loginId, String password, String email, String name, String birth) {
        registerMember(loginId, password, email, name, birth);
    }

    private void registerMember(String loginId, String password, String email, String name, String birth) {
        this.loginId = validateLoginId(loginId);
        this.password = password;
        this.email = validateEmail(email);
        this.name = name;
        this.birth = validateBirth(birth);
    }

    private String validateLoginId(String loginId) {
        if (loginId == null || loginId.trim().isEmpty()) {
            throw new IllegalArgumentException("아이디가 존재하지 않습니다.");
        }

        if (!loginId.matches("^[a-zA-Z0-9]{5,10}$")) {
            throw new IllegalArgumentException("아이디는 [영문 + 숫자] 5자 이상,10자 이하여야 합니다.");
        }

        return loginId;
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일이 존재하지 않습니다.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("이메일 형식이 일치하지 않습니다.");
        }

        return email;
    }

    private LocalDate validateBirth(String birth) {
        if (birth == null) {
            throw new IllegalArgumentException("생년월일이 존재하지 않습니다.");
        }

        try {
            LocalDate birthDate = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (birthDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("현재 날짜보다 앞선 날짜입니다.");
            }

            if (birthDate.isBefore(LocalDate.now().minusYears(150))) {
                throw new IllegalArgumentException("잘못된 생년월일입니다.");
            }

            return birthDate;
        } catch (Exception e) {
            throw new IllegalArgumentException("생년월일 형식에 문제가 발생했습니다.");
        }
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirth() {
        return birth;
    }
}
