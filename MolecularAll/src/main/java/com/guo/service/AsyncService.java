package com.guo.service;

import com.guo.uilts.PinyinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AsyncService {
    @Autowired
    private BreedService breedService;
    @Autowired
    private TraitService traitService;
    @Autowired
    private SnpService snpService;


    ArrayList<String> traits = new ArrayList<>();
    @Async
    public void processTrait(String traitType, Map<String, List<String>> request) {
        // 在这里编写异步处理性状指标的逻辑
        List<String> Trait = request.get(traitType);
        for (String t : Trait) {
            String trait = traitService.selectTrait(t);
            traits.add(trait);
        }
        System.out.println(traits);
    }



    //猪种
    ArrayList<String> breeds = new ArrayList<>();
    ArrayList<String> APsingleBreed = new ArrayList<>();
    ArrayList<String> REsingleBreed = new ArrayList<>();
    ArrayList<String> MEsingleBreed = new ArrayList<>();
    ArrayList<String> PRsingleBreed = new ArrayList<>();
    ArrayList<String> CAsingleBreed = new ArrayList<>();
    @Async
    public void processBreed(String BreedType, Map<String, List<String>> request) {
        // 在这里编写异步处理猪种的逻辑
        List<String> Breeds = request.get(BreedType);
        StringBuilder sb = new StringBuilder();
        for (String o : Breeds) {
            String s = PinyinUtil.toFirstLetter(o);
            if (BreedType.equals("reBreed")) {
                REsingleBreed.add(s);
            }
            // 添加其他判断...
            sb.append(s);
        }
        System.out.println("合并之后的个体为：" + sb.toString());
        String breed = breedService.queryBreed(sb.toString());
        breeds.add(breed + BreedType);
        System.out.println("猪种在服务器上的位置为：" + breeds);
    }

}
