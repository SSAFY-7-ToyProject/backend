package com.teamauc.diary.repository;

import com.teamauc.diary.domain.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FavoriteRepository {

    private final EntityManager em;

    // 즐겨찾기 추가
    public String regist(Favorite favorite) {
        em.persist(favorite);
        return favorite.getId();
    }



    // 즐겨찾기 id로 조회
    public Favorite find(String id){
        return em.find(Favorite.class,id);
    }

    // 유저의 즐겨찾기 조회
    public List<Favorite> findAllByUser(String uid){
        List<Favorite> favoriteList = em.createQuery("SELECT f FROM Favorite f WHERE f.user.uid = :uid").setParameter("uid",uid).getResultList();
        return favoriteList;
    }

    // 즐겨찾기 삭제
    public void delete(Favorite favorite) {
        em.remove(favorite);
    }

}
