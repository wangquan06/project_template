package com.moft.sysauth.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "account")
public class Account extends BModel {
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    private String mail;
    @Column(nullable = false)
    private Boolean valid;
}
