package com.guo.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SNP {
    private int Id;
    private String Gene;
    private String Trait;
    private int Chr;
    private String Position;
    private String SNPID;
    private String Allele;
    private String Reference;

}
