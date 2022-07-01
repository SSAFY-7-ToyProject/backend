package com.teamauc.diary.repository;

import com.teamauc.diary.domain.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryRepository {

    private final EntityManager em;

    public String create(Diary diary){

        em.persist(diary);

        return diary.getId();

    }

    public List<Diary> findAllDiaryOrderByRegTime(){
        return em.createQuery("SELECT d from Diary d WHERE d.secret = false ORDER BY d.regTime DESC", Diary.class).getResultList();
    }

    public List<Diary> findByUserEmail(String email){

        List<Diary> diaryList = em.createQuery("SELECT d from Diary d WHERE d.user.email = :email",Diary.class)
                .setParameter("email",email)
                .getResultList();

        return diaryList;
    }

    public List<Diary> findByUserId(String uid) {

        List<Diary> diaryList = em.createQuery("SELECT d from Diary d WHERE d.user.uid = :uid",Diary.class)
                .setParameter("uid",uid)
                .getResultList();

        return diaryList;
    }

    public List<Diary> findOpenByUserId(String uid) {

        List<Diary> diaryList = em.createQuery("SELECT d from Diary d WHERE d.user.uid = :uid AND d.secret = false",Diary.class)
                .setParameter("uid",uid)
                .getResultList();

        return diaryList;
    }

    public Diary findById(String id){

        return em.find(Diary.class,id);

    }

    public void delete (String id){

        em.remove( findById(id) );
    }


}
