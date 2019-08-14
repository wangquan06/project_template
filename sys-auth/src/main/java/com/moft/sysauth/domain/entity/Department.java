package com.moft.sysauth.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "department")
public class Department extends BModel {
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(nullable = false, length = 512)
    private String remark;
}
