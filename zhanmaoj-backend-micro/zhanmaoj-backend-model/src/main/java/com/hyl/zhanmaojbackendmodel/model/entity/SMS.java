package com.hyl.zhanmaojbackendmodel.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMS implements Serializable {

    private String status;

    private String phoneNumber;

    private String code;

    private int min;


    private static final long serialVersionUID = 1L;
}
