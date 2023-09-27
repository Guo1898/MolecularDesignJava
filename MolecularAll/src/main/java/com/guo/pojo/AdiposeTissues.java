package com.guo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdiposeTissues {
    private int Id;
    private String Genesprediction;
    private String pvalue;
    private String foldchange;
    private String log2foldchange;
    private String Significant;
    private String Start;
    private String End;
    private String CHR;
}
