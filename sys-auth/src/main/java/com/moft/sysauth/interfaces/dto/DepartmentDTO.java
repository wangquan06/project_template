package com.moft.sysauth.interfaces.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DepartmentDTO {
    private String code;
    private String name;
    private Date createDate;
    private Date updateDate;
    private String remark;
}
