package com.guo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guo.config.ThreadPoolConfig;
import com.guo.pojo.SNP;
import com.guo.pojo.User;
import com.guo.service.*;
import com.guo.uilts.*;
import org.apache.commons.compress.utils.IOUtils;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@RestController
public class DocumentController {

    @Autowired
    private BreedService breedService;
    @Autowired
    private TraitService traitService;
    @Autowired
    private SnpService snpService;
    @Autowired
    private SharedDataService sharedDataService;

    @Autowired
    private UserService userService;

    @GetMapping("/Snps")
    public List<SNP> getDataFromTableSnp() {
        return snpService.querySnp();
    }

    //性状指标
    private final Map<String, Map<String, List<String>>> traitDataMap = new ConcurrentHashMap<>();
    @PostMapping("/{traitType}/selectTrait")
    public ResponseEntity<?> processTrait(@PathVariable String traitType, @RequestBody Map<String, List<String>> request , @RequestHeader("Username") String username) {

        // 获取当前用户的特征数据，如果不存在则创建一个新的空Map
        Map<String, List<String>> userTraitData = traitDataMap.computeIfAbsent(username, k -> new ConcurrentHashMap<>());

        System.out.println(username);
        System.out.println(request);

        List<String> traitsList = userTraitData.getOrDefault(traitType, new ArrayList<>());

        // 将traitValue添加到数据列表中
        List<String> traitValues = request.get(traitType);
        for (String traitValue : traitValues) {
            String trait = traitService.selectTrait(traitValue);
            traitsList.add(trait);
        }

        // 将更新后的数据列表重新放入用户的特征数据中
        userTraitData.put(traitType, traitsList);


        //写到本地
        try  {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonFileName = userService.QueryNewfolder(username)+"/"+username;// 拼接文件路径
            objectMapper.writeValue(new File(jsonFileName), userTraitData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(userTraitData);

        return ResponseEntity.ok("Trait data updated successfully.");

    }



    //猪种
    private final Map<String, Map<String, List<String>>> breeds = new ConcurrentHashMap<>();
    Map<String, ArrayList<String>> userLists1 = new ConcurrentHashMap<>();
    Map<String, ArrayList<String>> userLists2 = new ConcurrentHashMap<>();
    Map<String, ArrayList<String>> userLists3 = new ConcurrentHashMap<>();
    Map<String, ArrayList<String>> userLists4 = new ConcurrentHashMap<>();
    Map<String, ArrayList<String>> userLists5 = new ConcurrentHashMap<>();
    @PostMapping("/{BreedType}/selectBreed")
    public void processBreed(@PathVariable String BreedType, @RequestBody Map<String, List<String>> request,@RequestHeader("Username") String username) {

        // 获取当前用户的特征数据，如果不存在则创建一个新的空Map
        Map<String, List<String>> userBreedData = breeds.computeIfAbsent(username, k -> new ConcurrentHashMap<>());
        ArrayList<String> REsingleBreed = userLists1.computeIfAbsent(username, k -> new ArrayList<>());
        ArrayList<String> MEsingleBreed = userLists2.computeIfAbsent(username, k -> new ArrayList<>());
        ArrayList<String> CAsingleBreed = userLists3.computeIfAbsent(username, k -> new ArrayList<>());
        ArrayList<String> APsingleBreed = userLists4.computeIfAbsent(username, k -> new ArrayList<>());
        ArrayList<String> PRsingleBreed = userLists5.computeIfAbsent(username, k -> new ArrayList<>());


//        // 最后，您可以将这个更新后的列表重新存储回Map中
//        userLists.put(username, singleBreedList);

        List<String> Breeds = request.get(BreedType);
        StringBuilder sb = new StringBuilder();
        System.out.println(Breeds);


        if (BreedType.equals("reBreed")){
            for (String breed : Breeds) {

                REsingleBreed.add(PinyinUtil.toFirstLetter(breed));
            }

            System.out.println(REsingleBreed+"wo///////////////////////////////////");
        }
        if (BreedType.equals("meBreed")){
            for (String breed : Breeds) {
                MEsingleBreed.add(PinyinUtil.toFirstLetter(breed));
            }
            System.out.println(MEsingleBreed+"///aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
        if (BreedType.equals("prBreed")){
            for (String breed : Breeds) {
                PRsingleBreed.add(PinyinUtil.toFirstLetter(breed));
            }
            System.out.println(PRsingleBreed);
        }
        if (BreedType.equals("caBreed")){
            for (String breed : Breeds) {
                CAsingleBreed.add(PinyinUtil.toFirstLetter(breed));
            }
            System.out.println(CAsingleBreed);
        }
        if (BreedType.equals("apBreed")){
            for (String breed : Breeds) {
                APsingleBreed.add(PinyinUtil.toFirstLetter(breed));
            }
            System.out.println(APsingleBreed);
        }

        for (String o :Breeds){
            String s = PinyinUtil.toFirstLetter(o);
            sb.append(s);

        }
        System.out.println(sb);
        String breed = breedService.queryBreed(sb.toString());
        userBreedData.put(BreedType,Arrays.asList(breed));
        System.out.println(breeds);

        //集合写到本地
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonFileName = userService.QueryNewfolder(username)+"/"+username+".Breed";// 拼接文件路径
            objectMapper.writeValue(new File(jsonFileName), userBreedData);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("================================================================================");
        System.out.println(userLists1);
        System.out.println(userLists2);
        System.out.println(userLists3);
        System.out.println(userLists4);
        System.out.println(userLists5);
        System.out.println("合并之后的个体为：" + sb.toString());
        System.out.println("猪种在服务器上的位置为：" + userBreedData);
    }



    //上传文件

    String FileAbsolutePath = "";
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file,@RequestHeader("Username") String username) throws IOException {

        String uploadDir=userService.QueryNewfolder(username);

        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                // 将文件名转换为小写，以便不区分大小写进行检查
                String lowercaseFilename = originalFilename.toLowerCase();

                // 检查文件是否是 .vcf 或 .vcf.gz 扩展名
                if (lowercaseFilename.endsWith(".vcf") || lowercaseFilename.endsWith(".vcf.gz")) {
                    Path copyLocation = Paths.get(uploadDir + File.separator + originalFilename);
                    Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                    // 文件上传成功，进行后续处理
                } else {
                    // 文件类型不符合要求，可以抛出异常或返回错误信息给用户
                    throw new IllegalArgumentException("只允许上传 .vcf 或 .vcf.gz 文件");
                }
            } else {
                // 文件名为空，可以抛出异常或返回错误信息给用户
                throw new IllegalArgumentException("文件名不能为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件上传过程中的异常
        }

        FileAbsolutePath = uploadDir+"/"+file.getOriginalFilename();
        System.out.println("上传的数据是： "+FileAbsolutePath);
        return "success";
    }



    //填写路径
    String filePathName = "";
    String a="";
    @PostMapping("/submitValue")
    public String submitValue(@RequestBody String data,@RequestHeader("Username") String username) throws IOException {

        filePathName = data;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(filePathName, Map.class);
        a = (String) map.get("AbsolutePath");
        System.out.println("传过来的数据是： "+filePathName);

        return "Success!";
    }

    //plink过滤
    @PostMapping("/plinkFilter")
    public String plinkFilter(@RequestBody String data,@RequestHeader("Username") String username) throws RserveException, JsonProcessingException {

        String PathName=userService.QueryNewfolder(username);
        RConnection c = new RConnection("10.75.9.100", 6311);
        c.eval("library(data.table)");
        System.out.println(data);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, Map.class);
        System.out.println(map);
        String maf = map.get("mafValue").toString();
        String geno = map.get("genoValue").toString();
        String hwe = map.get("hweValue").toString();

        c.eval("setwd(\""+PathName+"\")");
        c.eval("plink<-'/disk191/guojw/software/PLINK/plink'");
        if (a.length()>8){
            System.out.println(a);
            c.eval("a <- \""+a+"\"");
            c.eval("system(paste0(plink,' --allow-extra-chr --noweb -vcf ',a,' --geno " + geno + " --maf " + maf + " --hwe " + hwe + " --make-bed --out Geno'))");
            c.eval("system(paste0(plink,' --bfile Geno --recode --noweb  --out Geno'))");
        }else {
            c.eval("a <- \""+FileAbsolutePath+"\"");
            c.eval("system(paste0(plink,' --allow-extra-chr --noweb -vcf ',a,' --geno " + geno + " --maf " + maf + " --hwe " + hwe + " --make-bed --out Geno'))");
            c.eval("system(paste0(plink,' --bfile Geno --recode --noweb  --out Geno'))");
        }
        System.out.println("plink转换执行中......");


        c.eval("Geno <- fread(\"Geno.map\")");
        c.eval("colnames(Geno) <- c(\"CHR\",\"SNP_ID\",\"Morgan\",\"BP\")");
        c.eval("Geno$SNP_ID<-paste(\"chr\",Geno$CHR,\"_\",Geno$BP,sep = \"\")");
        c.eval("write.table(Geno,\"Geno.map\",row.names = F,col.names = F,quote = F,sep = \"\\t\")");

        c.close();
        System.out.println("success！执行成功！");
        System.out.println(PathName);
        return "success";
    }

    //QTL注释
    @PostMapping("/qtlComments")
    public String processRequest(@RequestBody String data,@RequestHeader("Username") String username) throws RserveException, REXPMismatchException, IOException {

        String PathName=userService.QueryNewfolder(username);

        RConnection c = new RConnection("10.75.9.100", 6311);
        System.out.println(PathName);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(data, Map.class);
        System.out.println(map);
        String interval = map.get("interval").toString();

        //创建文件
        String stPath = PathName+"/1First";
        String secondPath= PathName+"/2SecondSnpId";
        String thirdPath = PathName+"/3ThirdFinal";
        String forthPath = PathName+"/4ForthResult";
        String fifthPath = PathName+"/5FifthResult";
        Files.createDirectories(Paths.get(stPath));
        Files.createDirectories(Paths.get(secondPath));
        Files.createDirectories(Paths.get(thirdPath));
        Files.createDirectories(Paths.get(forthPath));
        Files.createDirectories(Paths.get(fifthPath));

        System.out.println(data);
        c.eval("library(GALLO)");
        c.eval("library(data.table)");
        c.eval("setwd(\"/disk195/guojw/MolecularDesign\")");
        c.eval("qtl.inp <- import_gff_gtf(db_file=\"QTLdb_pigSS11.gff\",file_type=\"gff\")");
        c.eval("setwd(\""+PathName+"\")");
        c.eval("QTLmarkers <- fread(\"Geno.bim\")");
        c.eval("colnames(QTLmarkers)<-c(\"CHR\",\"SNP_ID\",\"Morgan\",\"BP\",\"minor_allele\",\"major_allele\")");
        c.eval("out.qtls<-find_genes_qtls_around_markers(db_file=qtl.inp,\n" +
                "                                         marker_file=QTLmarkers, method = \"qtl\",\n" +
                "                                         marker = \"snp\", interval =" + interval + ", nThreads = 20)");
        c.eval("out.qtls$SNP_ID <- paste(\"chr\",out.qtls$CHR,\"_\",out.qtls$BP,sep = \"\")");
        c.eval("out.qtls <- out.qtls[,c(2,5,6,9,14)]");
        c.eval("write.table(out.qtls,\"out.qtls.txt\",row.names = F,col.names = T,quote = F,sep = \"\\t\")");

        c.close();
        System.out.println("success!qtl注释已执行完成！");
        System.out.println(PathName);
        return "success";
    }




    @Autowired
    private ReadData readData;

    //基因比对
    @PostMapping("/GeneAlignment")
    public String idealIndividual(@RequestBody String data,@RequestHeader("Username") String username) throws RserveException, IOException {

        String PathName=userService.QueryNewfolder(username);

        ArrayList<String> APsingleBreed = userLists4.get(username);
        ArrayList<String> REsingleBreed = userLists1.get(username);
        ArrayList<String> MEsingleBreed = userLists2.get(username);
        ArrayList<String> PRsingleBreed = userLists5.get(username);
        ArrayList<String> CAsingleBreed = userLists3.get(username);

        System.out.println(REsingleBreed);
        System.out.println(MEsingleBreed);

        RConnection c = new RConnection("10.75.9.100", 6311);
        System.out.println(data);
        System.out.println(PathName);

        ArrayList<String> reTrait = new ArrayList<>();
        ArrayList<String> prTrait = new ArrayList<>();
        ArrayList<String> meTrait = new ArrayList<>();
        ArrayList<String> caTrait = new ArrayList<>();
        ArrayList<String> apTrait = new ArrayList<>();

        ArrayList<String> reBreed = new ArrayList<>();
        ArrayList<String> prBreed = new ArrayList<>();
        ArrayList<String> meBreed = new ArrayList<>();
        ArrayList<String> caBreed = new ArrayList<>();
        ArrayList<String> apBreed = new ArrayList<>();

        String reFilename = "";
        String readRe = "";
        String prFilename = "";
        String readPr = "";
        String meFilename = "";
        String readMe = "";
        String caFilename = "";
        String readCa = "";
        String apFilename = "";
        String readAp = "";

        String reclassify="reproduction";
        String meclassify="meat";
        String caclassify="carcass";
        String prclassify="production";
        String apclassify="appearence";

        Map<String, List<String>> traits = readData.readTrait(username);
        Map<String, List<String>> breeds = readData.readBreed(username);

        //繁殖
        if (traits.containsKey("reTrait") && breeds.containsKey("reBreed")){
            reTrait.addAll(traits.get("reTrait"));
            reBreed.addAll(breeds.get("reBreed"));

            Pattern pattern1 = Pattern.compile("/disk195/guojw/BreedSql/(.*)\\.txt");
            for (String breed : reBreed) {
                Matcher matcher = pattern1.matcher(breed);
                if (matcher.find()) {
                    reFilename = matcher.group(1);
                    readRe = reFilename + " <- fread(\"" + breed + "\")";
                    System.out.println(readRe);
                }
            }
        }
        //生长
        if (traits.containsKey("prTrait") && breeds.containsKey("prBreed")){
            prTrait.addAll(traits.get("prTrait"));
            prBreed.addAll(breeds.get("prBreed"));

            Pattern pattern1 = Pattern.compile("/disk195/guojw/BreedSql/(.*)\\.txt");
            for (String breed : prBreed) {
                Matcher matcher = pattern1.matcher(breed);
                if (matcher.find()) {
                    prFilename = matcher.group(1);
                    readPr = prFilename + " <- fread(\"" + breed + "\")";
                    System.out.println(readPr);
                }
            }
        }
        //肉质
        if (traits.containsKey("meTrait") && breeds.containsKey("meBreed")){
            meTrait.addAll(traits.get("meTrait"));
            meBreed.addAll(breeds.get("meBreed"));

            Pattern pattern1 = Pattern.compile("/disk195/guojw/BreedSql/(.*)\\.txt");
            for (String breed : meBreed) {
                Matcher matcher = pattern1.matcher(breed);
                if (matcher.find()) {
                    meFilename = matcher.group(1);
                    readMe = meFilename + " <- fread(\"" + breed + "\")";
                    System.out.println(readMe);
                }
            }
        }
        //胴体
        if (traits.containsKey("caTrait") && breeds.containsKey("caBreed")){
            caTrait.addAll(traits.get("caTrait"));
            caBreed.addAll(breeds.get("caBreed"));

            Pattern pattern1 = Pattern.compile("/disk195/guojw/BreedSql/(.*)\\.txt");
            for (String breed : caBreed) {
                Matcher matcher = pattern1.matcher(breed);
                if (matcher.find()) {
                    caFilename = matcher.group(1);
                    readCa = caFilename + " <- fread(\"" + breed + "\")";
                    System.out.println(readCa);
                }
            }
        }
        //外貌
        if (traits.containsKey("apTrait") && breeds.containsKey("apBreed")){
            apTrait.addAll(traits.get("apTrait"));
            apBreed.addAll(breeds.get("apBreed"));

            Pattern pattern1 = Pattern.compile("/disk195/guojw/BreedSql/(.*)\\.txt");
            for (String breed : apBreed) {
                Matcher matcher = pattern1.matcher(breed);
                if (matcher.find()) {
                    apFilename = matcher.group(1);
                    readAp = apFilename + " <- fread(\"" + breed + "\")";
                    System.out.println(readAp);
                }
            }
        }


        c.close();

        if (reTrait.size() > 0) {

            MolecularDesign.properties(PathName,readRe, reTrait, reFilename, REsingleBreed, reclassify);
        }
        if (meTrait.size() > 0) {
            MolecularDesign.properties(PathName,readMe, meTrait, meFilename, MEsingleBreed, meclassify);
        }
        if (prTrait.size() > 0) {
            MolecularDesign.properties(PathName,readPr, prTrait, prFilename, PRsingleBreed, prclassify);
        }
        if (caTrait.size() > 0) {
            MolecularDesign.properties(PathName,readCa, caTrait, caFilename, CAsingleBreed, caclassify);
        }
        if (apTrait.size() > 0) {
            MolecularDesign.properties(PathName,readAp, apTrait, apFilename, APsingleBreed, apclassify);
        }


        return "success";

    }



    //  去除一因多效
    @PostMapping("/MultipleEffects")
    public String MultipleEffects(@RequestBody String data,@RequestHeader("Username") String username) throws RserveException, IOException {

        String PathName=userService.QueryNewfolder(username);

        RConnection c = new RConnection("10.75.9.100", 6311);
        c.eval("library(data.table)");

        System.out.println(PathName);
        String stPath = PathName+"/1First";
        File directory = new File(stPath);
        File[] files = directory.listFiles();
        System.out.println(stPath);

        List<String> TwoDigitName = new ArrayList<>();//文件名前两位大的一个集合


        for (File file : files) {
            String fileName = file.getName();//文件名
            System.out.println(fileName);
            String TwoDigit = fileName.substring(0, 2).toUpperCase();//文件名前两位大写
            TwoDigitName.add(TwoDigit);
            c.eval("setwd(\""+stPath+"\")");
            c.eval("" + TwoDigit + " <- fread(\"" + fileName + "\")");
            c.eval("" + TwoDigit + "<-" + TwoDigit + "[,c(1,4,5)]");
            c.eval("colnames(" + TwoDigit + ")<-c('SNP_ID','" + TwoDigit + "_allele' ,'" + TwoDigit + "_trait_ID')");
            c.eval("list <- (" + TwoDigit + ")");

        }

        System.out.println(TwoDigitName);

        if (files.length==1){
            for (int i = 0; i < TwoDigitName.size(); i++) {
                System.out.println(TwoDigitName.get(i));
                //但性状添加主效基因
                System.out.println("执行了");
                c.eval("setwd(\"/disk195/guojw/MolecularDesign\")");
                c.eval(""+TwoDigitName.get(i)+"ideal <- fread(\""+TwoDigitName.get(i)+".txt\",header = F)");
                c.eval("colnames("+TwoDigitName.get(i)+"ideal) <- c(\"SNP_ID\",\""+TwoDigitName.get(i)+"_allele\",\""+TwoDigitName.get(i)+"_trait_ID\")");
                c.eval("c <- rbind("+TwoDigitName.get(i)+","+TwoDigitName.get(i)+"ideal)");
                c.eval("setwd(\""+stPath+"\")");
                c.eval("colnames(c) <- c(\"SNP_ID\",\"allele\",\""+TwoDigitName.get(i)+"_trait_ID\")");
                c.eval("write.table(c,\""+TwoDigitName.get(i)+".txt\",row.names = F,sep = \"\\t\",quote = F)");
            }
        }else {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < TwoDigitName.size(); i++) {
                sb.append(TwoDigitName.get(i));
                if (i < TwoDigitName.size() - 1) {
                    sb.append(",");
                }
            }
            System.out.println(sb);
            c.eval("list <- list("+sb+")");

            c.eval("all <- list[[1]]");
            c.eval("for (i in 2:length(list)) {all <- merge(all, list[[i]], by.x =\"SNP_ID\",by.y =\"SNP_ID\",all = TRUE)}");

            List<String> subsets = new ArrayList<>();
            for (int i = 0; i < TwoDigitName.size(); i++) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append("subset(all,");
                for (int j = 0; j < TwoDigitName.size(); j++) {
                    if (j > 0) {
                        sb1.append("&");
                    }
                    if (i == j) {
                        sb1.append("!is.na(all$").append(TwoDigitName.get(j)).append("_trait_ID)");
                    } else {
                        sb1.append("is.na(all$").append(TwoDigitName.get(j)).append("_trait_ID)");
                    }
                }
                sb1.append(")");
                System.out.println(sb1);
                String subsetName = Character.toString((char) ('a' + i));

                c.eval(""+subsetName +" <- "+sb1.toString()+"");
                subsets.add(subsetName);
            }

            System.out.println(subsets);
            String fina = String.join(",", subsets);
            System.out.println(fina);
            c.eval("all <- rbind("+fina+")");

            for (int i = 0; i < TwoDigitName.size(); i++) {
                System.out.println(TwoDigitName.get(i));
                c.eval(""+TwoDigitName.get(i)+" <- all[,c(\"SNP_ID\",\""+TwoDigitName.get(i)+"_allele\",\""+TwoDigitName.get(i)+"_trait_ID\")]");
                c.eval(""+TwoDigitName.get(i)+" <- subset("+TwoDigitName.get(i)+",!is.na("+TwoDigitName.get(i)+"$"+TwoDigitName.get(i)+"_allele))");
                c.eval("setwd(\""+stPath+"\")");
                c.eval("colnames("+TwoDigitName.get(i)+") <- c(\"SNP_ID\",\"allele\",\""+TwoDigitName.get(i)+"_trait_ID\")");

                //添加主效基因
                c.eval("setwd(\"/disk195/guojw/MolecularDesign\")");
                c.eval(""+TwoDigitName.get(i)+"ideal <- fread(\""+TwoDigitName.get(i)+".txt\",header = F)");
                c.eval("colnames("+TwoDigitName.get(i)+"ideal) <- c(\"SNP_ID\",\"allele\",\""+TwoDigitName.get(i)+"_trait_ID\")");
                c.eval("c <- rbind("+TwoDigitName.get(i)+","+TwoDigitName.get(i)+"ideal)");
                c.eval("setwd(\""+stPath+"\")");
                c.eval("write.table(c,\""+TwoDigitName.get(i)+".txt\",row.names = F,sep = \"\\t\",quote = F)");

            }
        }
        System.out.println("去除一因多效成功！success!");
        c.close();
        return "success";
    }


    //生成理想个体
    @PostMapping("/makeIdeaIndividual")
    public String ideaIndividual(@RequestBody String data,@RequestHeader("Username") String username) throws RserveException {
        String PathName=userService.QueryNewfolder(username);
        System.out.println(data);
        RConnection c = new RConnection("10.75.9.100", 6311);


        c.eval("library(data.table)");
        //知道文件夹下面有哪些文件
        ArrayList<Object> list = new ArrayList<>();
        String stPath = PathName+"/1First";
        String secondPath= PathName+"/2SecondSnpId";
        String thirdPath = PathName+"/3ThirdFinal";

        File directory = new File(stPath);
        File[] files = directory.listFiles();
        //现实个体
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                String Two = fileName.substring(0, 2).toUpperCase();
                System.out.println(Two);
                System.out.println(fileName);
                c.eval("setwd(\""+stPath+"\")");
                c.eval("" + Two + " <- fread(\"" + fileName + "\")");
                c.eval("setwd(\""+secondPath+"\")");
                c.eval("" + Two + "_snpid  <- " + Two + "$SNP_ID");
                c.eval("write.table(" + Two + "_snpid,\"" + Two + "_snpid.txt\",row.names = F,col.names = F,quote = F,sep = \"\\t\")");
                System.out.println("" + Two + "snipid已经被执行了...");

                c.eval("plink<-'/disk191/guojw/software/PLINK/plink'");

                c.eval("setwd(\""+PathName+"\")");
                c.eval("system(paste0(plink,' --file "+PathName +"/Geno  --extract  " + secondPath + "/" + Two + "_snpid.txt --recode --out " + secondPath + "/" + Two + "_Real'))");


                System.out.println("" + Two + "Real文件已经被写入......");

                c.eval("setwd(\""+secondPath+"\")");
                //map文件
                c.eval("a <- substr(" + Two + "$SNP_ID,4,5)");
                c.eval("a <- gsub(\"_\",\"\",a)");
                c.eval("b <- substr(" + Two + "$SNP_ID,6,20)");
                c.eval("b <- gsub(\"_\",\"\",b)");
                c.eval("a <- as.data.frame(a)");
                c.eval("a[,2] <- " + Two + "$SNP_ID");
                c.eval("a[,3] <- c(0)");
                c.eval("a[,4] <- b");
                c.eval("write.table(a,\"" + Two + "_dream.map\",col.names = F,row.names = F,quote = F,sep = \"\\t\")");
                c.eval("write.table("+Two+",\"" + Two + "_________.txt\",col.names = T,row.names = F,quote = F,sep = \"\\t\")");
                System.out.println("map文件已经被写入...");
                //ped文件
                c.eval("ped <- " + Two + "$allele");
                c.eval("ped <- as.data.frame(ped)");
                c.eval("ped$ped <- paste(ped$ped,ped$ped)");
                c.eval("ped$ped <- as.array(ped$ped)");
                c.eval("a <- ped$ped");
                c.eval("a <- as.character(a)");
                c.eval("b <- c(\"XXidea\", \"XXidea\", \"0\", \"0\", \"0\", \"-9\")");
                c.eval("b <- as.character(b)");
                c.eval("c <- c(b,a)");
                c.eval("write.table(t(c),\"" + Two + "_dream.ped\",col.names = F,row.names = F,quote = F,sep = \" \")");
                System.out.println("ped文件已经被写入...");



                //合并个体
                c.eval("setwd(\""+secondPath+"\")");
                c.eval("a <- \"" + Two + "_Real\"");
                c.eval("b <- \""+thirdPath+"/" + Two + "_final\"");
                c.eval("c <- \"" + Two + "_final\"");
                c.eval("system(paste0(plink,' --file ',a,' --merge " + Two + "_dream.ped " + Two + "_dream.map --make-bed --out ',b))");

                c.eval("setwd(\""+thirdPath+"\")");
                //转vcf
                c.eval("system(paste0(plink,\" --bfile \",c,\" --recode vcf --out \",c))");
                //转ped
                c.eval("system(paste0(plink,\" --vcf \",c,\".vcf --recode --out \",c))");
                //计算ibs
                c.eval("system(paste0(plink,\" --file \",c,\" --distance ibs --out \",c))");
            }
        }
        c.close();
        return "success";
    }


    //获得结果集
    @GetMapping("/ResultDisplay")
    public List<List<String>> result(@RequestHeader("Username") String username) throws RserveException {
        String PathName=userService.QueryNewfolder(username);
        RConnection c = new RConnection("10.75.9.100", 6311);
        c.eval("library(data.table)");


        String secondPath= PathName+"/2SecondSnpId";
        String forthPath = PathName+"/4ForthResult";

        //知道文件夹下面有哪些文件
        File directory = new File(secondPath);
        ArrayList<Object> list = new ArrayList<>();
        File[] files = directory.listFiles();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            if (fileName.contains("dream.map")) {

                System.out.println(fileName);
                String Two = fileName.substring(0, 2).toUpperCase();
                System.out.println(Two);
                c.eval("setwd(\""+secondPath+"\")");
                c.eval("" + Two + "map <- fread(\"" + Two + "_dream.map\")");
                c.eval("" + Two + "map <- " + Two + "map[c(1:20),]");
                c.eval("colnames(" + Two + "map) <- c(\"" + Two + "_Chr\",\"" + Two + "_Snp_id\",\"" + Two + "_Morgan\",\"" + Two + "_BP\")");
                c.eval("" + Two + "ped <- fread(\"" + Two + "_dream.ped\")");
                c.eval("ncol_needed <- 46");
                c.eval("if (ncol(" + Two + "ped) < ncol_needed) {\n" +
                        "                new_cols <- paste0(\"V\", (ncol(" + Two + "ped)+1):ncol_needed)\n" +
                        "                " + Two + "ped[, (new_cols) := NA]\n" +
                        "            }");
                c.eval("a2 <- as.matrix("+Two+"ped[1,c(7:46)])");
                c.eval("index <- rep(c(TRUE, FALSE), length(a2)/2)");
                c.eval("a2 <- as.matrix(a2[index])");
                c.eval("colnames(a2) <- \""+Two+"_allele\"");
                c.eval("" + Two + "Display <- cbind(" + Two + "map,a2)");
                System.out.println(Two + "被执行了");
                list.add(Two + "Display");

            }

        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        System.out.println(sb);
        String s = "result <- cbind(" + sb.toString() + ")";
        c.eval(s);
        System.out.println(s + "被执行...");
        c.eval("setwd(\""+forthPath+"\")");
        c.eval("write.table(result,\"result.txt\",col.names = T,row.names = F,quote = F,sep = \"\\t\")");
        c.close();
        System.out.println("susscess!");


        List<List<String>> matrix = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(""+forthPath+"/result.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                List<String> row = new ArrayList<>(Arrays.asList(values));
                matrix.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    //理想个体下载
    @GetMapping("/IdealIndividualDownload")
    public String ResultDown(@RequestParam String filename, HttpServletResponse response,@RequestHeader("Username") String username) throws IOException {

        String PathName=userService.QueryNewfolder(username);

        ArrayList<String> list = new ArrayList<>();
        String secondPath= PathName+"/2SecondSnpId";
        String forthPath = PathName+"/4ForthResult";
        File directory = new File(secondPath);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains("dream.ped")|files[i].getName().contains("dream.map")){
                System.out.println(files[i].getName());
                list.add(files[i].getName());
            }
        }
        System.out.println(list);
        String path = secondPath;
        String zipPath = ""+forthPath+"/out.zip";
        Zip.zipFiles(path,list,zipPath);


        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
        // 获取zip文件的路径

        // 将zip文件作为附件返回给前端
        Resource resource = new FileSystemResource(zipPath);
        InputStream inputStream = resource.getInputStream();
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();

        return "success!";
    }


    @PostMapping("/traitScale")
    public String traitScale(@RequestBody String requestData,@RequestHeader("Username") String username) throws RserveException, JsonProcessingException {

        String PathName=userService.QueryNewfolder(username);
        RConnection c = new RConnection("10.75.9.100", 6311);
        c.eval("library(data.table)");

        String thirdPath = PathName+"/3ThirdFinal";
        String forthPath = PathName+"/4ForthResult";
        System.out.println(requestData);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(requestData, Map.class);

        System.out.println(map);
        List<String> list = new ArrayList<>();

        File directory = new File(thirdPath);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            if (fileName.contains("mibs.id")){
                System.out.println(fileName);
                String Two = fileName.substring(0, 2).toUpperCase();
                System.out.println(Two);
                c.eval("setwd(\""+thirdPath+"\")");

                c.eval("lines <- readLines(\""+Two+"_final.mibs\")");
                c.eval("data <- lapply(lines, function(x) as.numeric(unlist(strsplit(x, \"\\t\"))))");
                c.eval("max_length <- max(sapply(data, length))");
                c.eval("data_matrix <- t(sapply(data, function(x) c(x, rep(NA, max_length - length(x)))))");
                c.eval("d <- read.table(\""+Two+"_final.mibs.id\",sep = \"\\t\", header = FALSE)");
                c.eval("ID <- d[,1]");
                c.eval("row.names(data_matrix) <- ID");
                c.eval("colnames(data_matrix) <- ID");
                c.eval("final <- as.matrix(t(data_matrix[\"XXidea\",]))");
                c.eval("row.names(final) <- \""+Two+"\"");

                Object value = map.get(Two.toLowerCase() + "value");

                double va;
                if (value instanceof Integer) {
                    va = ((Integer) value).doubleValue(); // 如果是整数，转换为小数
                } else if (value instanceof Double) {
                    va = (Double) value; // 如果是小数，直接使用
                } else {
                    // 其他情况，你可以选择默认值或者其他处理
                    va = 0.0; // 默认值为0.0
                }
                System.out.println(va);

                if (va > 0){
                    c.eval(""+Two+"_scaleResult <- t(final*"+va+")");
                    c.eval("write.table("+Two+"_scaleResult,\""+Two+"_Final.txt\",col.names = T,row.names = F,quote = F,sep = \"\\t\")");
                    list.add(Two+"_scaleResult");
                }else {
                    System.out.println("不匹配，不执行");
                }

            }
        }
        System.out.println(list);
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            sb.append(list.get(i));
            sb1.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(",");
                sb1.append("+");
            }
        }

        c.eval("result1 <- cbind("+sb1+")");
        c.eval("colnames(result1) <- \"ALL\"");
        c.eval("result2 <- cbind("+sb+")");
        c.eval("result <- cbind(ID,result2,result1)");
        c.eval("setwd(\""+forthPath+"\")");
        c.eval("write.table(result,\"ALL_Final.txt\",col.names = T,row.names = F,quote = F,sep = \"\\t\")");

        c.close();
        System.out.println("success!");
        return "success!";
    }

    @GetMapping("/traitScaleDisplay")
    public List<List<String>> traitScaleDisplay(@RequestHeader("Username") String username)  {

        String PathName=userService.QueryNewfolder(username);
        String forthPath = PathName+"/4ForthResult";
        List<List<String>> matrix = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(""+forthPath+"/ALL_Final.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                List<String> row = new ArrayList<>(Arrays.asList(values));
                matrix.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("success!");
        return matrix;
    }

    @GetMapping("/traitScaleDownload")
    public void downloadFile(HttpServletResponse response,@RequestHeader("Username") String username) throws IOException {

        String PathName=userService.QueryNewfolder(username);
        String forthPath = PathName+"/4ForthResult";
        Resource resource = new FileSystemResource(""+forthPath+"/ALL_Final.txt");
        InputStream inputStream = resource.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        String content = new String(bytes, StandardCharsets.UTF_8);

        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=Result.txt");
        response.getOutputStream().write(content.getBytes(StandardCharsets.UTF_8));
    }

}
