package com.teamauc.diary.service;

import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.Weather;
import com.teamauc.diary.exception.InvalidApproachException;
import com.teamauc.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;


    public List<Diary> findByUserEmail (String email) {

        List<Diary> diaryList = diaryRepository.findByUserEmail(email);

        return diaryList;
    }

    public  List<Diary>  findByUserId(String uid) {
        List<Diary> diaryList = diaryRepository.findByUserId(uid);

        return diaryList;
    }

    public Diary findByDiaryId (String id){

        Diary diary = diaryRepository.findById(id);

        return diary;
    }

     @Transactional
     public String update(String id, Weather weather, boolean secret, String title, String content) {

        Diary diary = diaryRepository.findById(id);

        if(diary == null) {
            throw new InvalidApproachException("존재하지 않는 일기입니다.");
        }
        else{
            diary.setWeather(weather);
            diary.setSecret(secret);
            diary.setTitle(title);
            diary.setContent(content);

            return diary.getId();
        }

     }

    @Transactional
    public String regist (Diary diary){

      return diaryRepository.create(diary);

    }

    @Transactional
    public void delete (String id) {

        diaryRepository.delete(id);

    }


}
