package com.moft.sysauth.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class BModel {
    @Id
    @Column(name = "uuid", unique = true, nullable = false, length = 50)
    private String uuid;
}
