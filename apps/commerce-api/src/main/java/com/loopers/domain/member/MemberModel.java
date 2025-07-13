package com.loopers.domain.member;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class MemberModel extends BaseEntity {

    private String loginId;

    private String password;

    private String email;

    private String name;

}
