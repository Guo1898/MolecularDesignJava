package com.guo.uilts;

public class AlleleAlignment {

    public static String MajorAllele(String[] Arr){

        StringBuilder majorAllele = new StringBuilder();
        StringBuilder majorAlleleFinal = new StringBuilder();
        System.out.println(PinyinUtil.toFirstLetter(Arr[0]));
        if (Arr.length==1){
            majorAllele.append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[0]));
        }else if (Arr.length==2){
            majorAllele.append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[0])).append("&").
                    append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[1]));
        }else {
            majorAllele.append("(").append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[0])).append("&").
                    append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[1])).append(")").append("|").append("(").append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[0])).append("&").
                    append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[2])).append(")").append("|").append("(").append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[1])).append("&").
                    append("major_allele==").append(PinyinUtil.toFirstLetter(Arr[2])).append(")");
        }

        majorAlleleFinal.append("BLFZ<-subset(R,").append(majorAllele).append(")");
        String maxResult = majorAlleleFinal.toString();
        return maxResult;
    }

    public static String MinorAllele(String[] Arr){
        StringBuilder MinorAllele = new StringBuilder();
        StringBuilder MinorAlleleFinal = new StringBuilder();
        if (Arr.length==1){
            MinorAllele.append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[0]));
        }else if (Arr.length==2){
            MinorAllele.append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[0])).append("&").
                    append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[1]));
        }else {
            MinorAllele.append("(").append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[0])).append("&").
                    append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[1])).append(")").append("|").append("(").append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[0])).append("&").
                    append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[2])).append(")").append("|").append("(").append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[1])).append("&").
                    append("minor_allele==").append(PinyinUtil.toFirstLetter(Arr[2])).append(")");
        }

        MinorAlleleFinal.append("minor<-subset(R,").append(MinorAllele).append(")");
        String minResult = MinorAlleleFinal.toString();
        return minResult;
    }

}
