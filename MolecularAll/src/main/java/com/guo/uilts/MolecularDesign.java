package com.guo.uilts;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MolecularDesign {
    public static String properties(String PathName,String readFile,ArrayList<String> CharacterType,String fileName,ArrayList<String> singleBreed,String typeClassify ) throws RserveException, IOException {

        RConnection c = new RConnection("10.75.9.100", 6311);
        c.eval("library(data.table)");

        //设置路径
        c.eval("setwd(\""+PathName+"\")");
        c.eval("out.qtls <- fread(\"out.qtls.txt\")");
        System.out.println("读取out.qtls文件成功...");

        //dlk <- fread("/disk195/guojw/BreedSql/dlk.txt")
        c.eval(readFile);
        System.out.println(readFile+"被执行...");

        //生成qtl_reproduction<-subset(Meat,trait_ID=="Litter size" | trait_ID=="Total number born alive" | trait_ID=="Litter weight piglets born alive")
        String traitID="";
        for (int i = 0; i <CharacterType.size() ; i++) {
            if (i>=1){
                traitID+=" | ";
            }
            traitID += "trait_ID==\"" + CharacterType.get(i) + "\"";
        }
        String qtl_Meat="qtl_reproduction<-subset(out.qtls ,"+ traitID + ")";
        c.eval(qtl_Meat);
        System.out.println(qtl_Meat+"被执行了...");
        c.eval("R<-merge("+ fileName +",qtl_reproduction,by.x = \"SNP_ID\",by.y = \"SNP_ID\")");
        System.out.println("执行了合并基因集......");
        c.eval("write.table(R,\"aaaaaRRR.txt\")");


        //major Allele
        String majorMeResult = AlleleAlignment.MajorAllele(singleBreed.toArray(new String[0]));
        c.eval(majorMeResult);
        System.out.println(majorMeResult+"被执行了...");
        //minor Allele
        String minorMeResult = AlleleAlignment.MinorAllele(singleBreed.toArray(new String[0]));
        c.eval(minorMeResult);
        System.out.println(minorMeResult+"被执行了...");
        c.eval("write.table(BLFZ,\"BLFZ.txt\",quote = F,sep = \" \")");
        c.eval("write.table(minor,\"minor.txt\",quote = F,sep = \" \")");


        if (singleBreed.size()==1){
            c.eval("BLFZ_bl<-BLFZ[,c(1,2,3,6,8)]");
            c.eval("minor_bl<-minor[,c(1,2,3,5,8)]");
        }
        if (singleBreed.size()==2){
            c.eval("BLFZ_bl<-BLFZ[,c(1,2,3,7,9)]");
            c.eval("minor_bl<-minor[,c(1,2,3,6,9)]");
        }
        if (singleBreed.size()==3){
            c.eval("BLFZ_bl<-BLFZ[,c(1,2,3,8,10)]");
            c.eval("minor_bl<-minor[,c(1,2,3,7,10)]");
        }

        c.eval("names(BLFZ_bl) <- gsub(\"major_allele\", \"allele\", names(BLFZ_bl))");
        c.eval("names(minor_bl) <- gsub(\"minor_allele\", \"allele\", names(minor_bl))");
        c.eval(""+typeClassify+"<-rbind(BLFZ_bl,minor_bl)");
        c.eval(""+typeClassify+"<- "+typeClassify+"[!duplicated("+typeClassify+"[,c(1)]),]");


        String stPath = PathName+"/1First";


        c.eval("setwd(\""+stPath+"\")");

        String TwoDigit1 = typeClassify.substring(0, 2).toUpperCase();
        c.eval("write.table("+typeClassify+",\""+TwoDigit1+".txt\",row.names = F,col.names = T,quote = F,sep = \"\\t\")");
        System.out.println("success");
        c.close();

        return "success";

    }
}
