package com.guo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Breed {
    private int id;
    private String breedNaZn;
    private String breedNaEn;
    private String location;

}
