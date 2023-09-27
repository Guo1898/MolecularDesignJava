package com.guo.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataShare {
    private int Id;
    private String AlignmentID;
    private String ESIMode;
    private String Adducttype;
    private String Metabolitename;
    private double RTmin;
    private double mz;
    private String Formula;
    private String Ontology;
    private double VIP;
    private double FC;
    private double Pvalue;
}

