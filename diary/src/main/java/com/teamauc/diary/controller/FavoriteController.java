package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Favorite;
import com.teamauc.diary.domain.Gender;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.dto.MessageResponseDto;
import com.teamauc.diary.dto.ResultDto;
import com.teamauc.diary.exception.InvalidApproachException;
import com.teamauc.diary.service.FavoriteService;
import com.teamauc.diary.service.JwtService;
import com.teamauc.diary.service.UserService;
import com.teamauc.diary.util.SHA256;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final JwtService jwtService;

    // 즐겨찾기 추가
    @PostMapping
    public RegistFavoriteResponseDto registFavorite(@RequestBody RegistFavoriteRequestDto request) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();
        User user = userService.searchUserById(request.getUid());
        User friend = userService.searchUserById(request.getFriendUid());
        if(user==null || friend==null) throw new InvalidApproachException("존재하지 않는 사용자");
        if(!user.getUid().equals(currentUid)) throw new InvalidApproachException("잘못된 접근입니다");
        if(user.getUid()==friend.getUid()) throw new InvalidApproachException("자신을 즐겨찾기 할 수 없음");

        String favoriteId = favoriteService.regist(Favorite.createFriend(user,friend));
        return new RegistFavoriteResponseDto(favoriteId);
    }

    // 사용자 즐겨찾기 목록 조회
    @GetMapping("/user/{uid}")
    public ResultDto searchFavoritesByUserId(@PathVariable("uid") String uid) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        // 본인의 목록만 조회 가능
        String currentUid = jwtService.getUserId();
        if(currentUid == null) throw new InvalidApproachException();
        if(!currentUid.equals(uid)) throw new InvalidApproachException();

        List<SearchFavoriteResponseDto> favoriteList = favoriteService.findAllByUser(uid)
                .stream()
                .map(favorite -> new SearchFavoriteResponseDto(favorite))
                .collect(Collectors.toList());
        return new ResultDto(favoriteList);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{id}")
    public MessageResponseDto deleteFavorite(@PathVariable("id") String id) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        // 본인의 즐겨찾기만 삭제 가능
        String currentUid = jwtService.getUserId();
        Favorite favorite = favoriteService.findOne(id);
        if(currentUid == null) throw new InvalidApproachException();
        if(!currentUid.equals(favorite.getUser().getUid())) throw new InvalidApproachException();


        favoriteService.delete(favorite);
        return new MessageResponseDto("삭제 완료");
    }

    @Data
    static class RegistFavoriteRequestDto {
        private String uid;
        private String friendUid;
    }

    @Data
    @AllArgsConstructor
    static class RegistFavoriteResponseDto {
        private String id;
    }

    @Data
    @AllArgsConstructor
    static class SearchFavoriteResponseDto {
        private String id;
        private String uid;
        private String friendUid;

        private String friendName;

        private Gender gender;

        public SearchFavoriteResponseDto(Favorite favorite) {
            this.id = favorite.getId();
            this.uid = favorite.getUser().getUid();
            this.friendUid = favorite.getFriend().getUid();
            this.friendName = favorite.getFriend().getName();
            this.gender = favorite.getFriend().getGender();
        }
    }

}
