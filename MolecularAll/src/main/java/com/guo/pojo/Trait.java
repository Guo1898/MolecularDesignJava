package com.guo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trait {
    private int id ;
    private String traitName;
    private String trait;
    private String classify;
}
