package com.example.mujeans.service;

import com.example.mujeans.model.NickDTO;
import com.example.mujeans.repository.nick.NickNameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.List;

@Log4j2
@Service("NickNameService")
@RequiredArgsConstructor
public class NickNameService {
    private final NickNameRepository nickNameRepository;

    public String getNickName(){
        String nickName = "";
        List <NickDTO> list1 = nickNameRepository.findByDepth(1);
        List <NickDTO> list2 = nickNameRepository.findByDepth(2);
        List <NickDTO> list3 = nickNameRepository.findByDepth(3);

        Random random = new Random();
        int randomNumber1 = random.nextInt(list1.size()-1) + 1;
        int randomNumber2 = random.nextInt(list2.size()-1) + 1;
        int randomNumber3 = random.nextInt(list3.size()-1) + 1;
        int randomNumber4 = random.nextInt(2) + 1;
        if (randomNumber4 == 1){
            nickName = list1.get(randomNumber1).getContent() + list2.get(randomNumber2).getContent() + list3.get(randomNumber3).getContent();
        }else{
            nickName = list2.get(randomNumber2).getContent() + list1.get(randomNumber1).getContent() + list3.get(randomNumber3).getContent();
        }

        return nickName;
    }
}
