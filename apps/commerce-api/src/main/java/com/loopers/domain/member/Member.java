package com.loopers.domain.member;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CommonErrorType;
import com.loopers.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Column(name = "login_id", nullable = false, length = 10, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "point", nullable = false)
    private Long point = 0L;

    protected Member() {
    }

    private Member(String loginId, String password, String email, String name, LocalDate birth, String gender) {
        this.loginId = validateLoginId(loginId);
        this.password = password;
        this.email = validateEmail(email);
        this.name = name;
        this.birth = validateBirth(birth);
        this.gender = validateGender(gender);
    }


    public static Member registerMember(String loginId, String password, String email, String name, LocalDate birth, String gender) {
        return new Member(loginId, password, email, name, birth, gender);
    }

    private String validateLoginId(String loginId) {
        if (loginId == null || loginId.trim().isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "아이디가 존재하지 않습니다.");
        }

        if (!loginId.matches("^[a-zA-Z0-9]{1,10}$")) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "아이디는 [영문 + 숫자] 10자 이하여야 합니다.");
        }

        return loginId;
    }

    private String validateGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "성별이 존재하지 않습니다.");
        }

        if (!gender.equals("M") && !gender.equals("F")) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "성별은 'M' 또는 'F'만 허용됩니다.");
        }

        return gender;
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "이메일이 존재하지 않습니다.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+(?:\\.[A-Za-z0-9]+)*\\.[A-Za-z]{2,}$")) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "이메일 형식이 일치하지 않습니다.");
        }

        return email;
    }

    private LocalDate validateBirth(LocalDate birth) {
        if (birth == null) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "생년월일이 존재하지 않습니다.");
        }

        // birth가 1997-12-04 형식이 아니면 예외
        if (birth.toString().length() != 10 || !birth.toString().matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "생년월일 형식이 일치하지 않습니다. yyyy-MM-dd 형식이어야 합니다.");
        }

        if (birth.isAfter(LocalDate.now())) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "현재 날짜보다 앞선 날짜입니다.");
        }

        if (birth.isBefore(LocalDate.now().minusYears(150))) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "잘못된 생년월일입니다.");
        }

        return birth;
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

    public String getGender() {
        return gender;
    }

    public long getPoint() {
        return point;
    }

    // 포인트 충전
    public void chargePoint(Long amount) {
        if (amount == null || amount <= 0) {
            throw new CoreException(CommonErrorType.BAD_REQUEST, "충전할 포인트는 0보다 커야 합니다.");
        }

        this.point += amount;
    }
}
