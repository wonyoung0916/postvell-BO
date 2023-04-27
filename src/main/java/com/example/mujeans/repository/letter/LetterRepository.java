package com.example.mujeans.repository.letter;

import com.example.mujeans.model.LetterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<LetterDTO, Integer> {

    LetterDTO save(LetterDTO letterDTO);

    LetterDTO findById(int letSeq);

}