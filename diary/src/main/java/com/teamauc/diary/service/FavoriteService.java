package com.teamauc.diary.service;

import com.teamauc.diary.domain.Favorite;
import com.teamauc.diary.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    // 즐겨찾기 추가
    @Transactional
    public String regist(Favorite favorite){
        return favoriteRepository.regist(favorite);
    }

    // 즐겨찾기 id로 조회
    public Favorite findOne(String id) {
        return favoriteRepository.find(id);
    }

    // 사용자 즐겨찾기 조회
    public List<Favorite> findAllByUser(String uid){
        return favoriteRepository.findAllByUser(uid);
    }

    // 즐겨찾기 삭제
    @Transactional
    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}
