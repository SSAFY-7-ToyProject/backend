package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.domain.Weather;
import com.teamauc.diary.dto.MessageResponseDto;
import com.teamauc.diary.dto.ResultDto;
import com.teamauc.diary.exception.InvalidApproachException;
import com.teamauc.diary.exception.UnauthorizedException;
import com.teamauc.diary.service.DiaryService;
import com.teamauc.diary.service.JwtService;
import com.teamauc.diary.service.UserService;
import com.teamauc.diary.util.SHA256;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final JwtService jwtService;
    private final DiaryService diaryService;
    private final UserService userService;

    @PostMapping
    public CreateDiaryResponseDto createDiary(@RequestBody @Valid CreateDiaryRequestDto request) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        Diary diary = Diary.createDiary(
                userService.searchUserById(jwtService.getUserId()),
                request.getWeather(),
                LocalDateTime.now(),
                request.isSecret(),
                request.getBackgroundColor(),
                request.getTitle(),
                request.getContent());

        diaryService.regist(diary);

        return new CreateDiaryResponseDto(diary.getId(),diary.getRegTime());
    }

    @GetMapping("/list")
    public ResultDto readAllDiary(){
        List<ReadDiaryResponseDto> diaryList = diaryService.findAllDiaryOrderByRegTime().stream().map(diary -> new ReadDiaryResponseDto(diary)).collect(Collectors.toList());
        return new ResultDto(diaryList);
    }

    @GetMapping("/user/{uid}")
    public ResultDto readDiaryByEmail(@PathVariable("uid") String uid) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("인증 실패");

        String currentUid = jwtService.getUserId();

        boolean isMine = currentUid.equals(uid);

        List<ReadDiaryResponseDto> diaryList = new ArrayList<>();

        if (isMine) {
            diaryList = diaryService.findByUserId(uid).stream().map(diary -> new ReadDiaryResponseDto(diary)).collect(Collectors.toList());
        } else {
            diaryList = diaryService.findOpenByUserId(uid).stream().map(diary -> new ReadDiaryResponseDto(diary)).collect(Collectors.toList());
        }

        return new ResultDto(diaryList);

    }

    @GetMapping("/{id}")
    public ResultDto readDiaryById(@PathVariable("id") String id) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();

        Diary diary = diaryService.findByDiaryId(id);

        boolean isMine = currentUid.equals(diary.getUser().getUid());

        if (!isMine&&diary.isSecret()) {
            throw new UnauthorizedException();
        }


        return new ResultDto(new ReadDiaryResponseDto(diary));

    }

    @PutMapping("/{diaryId}")
    public MessageResponseDto UpdateDiary(@PathVariable("diaryId") String id, @RequestBody @Valid UpdateDiaryRequestDto request) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();

        Diary diary = diaryService.findByDiaryId(id);

        boolean isMine = currentUid.equals(diary.getUser().getUid());

        if(!isMine) throw new UnauthorizedException("본인의 일기가 아닙니다.");

        diaryService.update(id, request.getWeather(), request.isSecret(), request.getBackgroundColor(), request.getTitle(), request.getContent());

        return new MessageResponseDto("수정 완료");
    }

    @DeleteMapping("{diaryId}")
    public MessageResponseDto DeleteDiary(@PathVariable("diaryId") String id) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();

        Diary diary = diaryService.findByDiaryId(id);

        boolean isMine = currentUid.equals(diary.getUser().getUid());

        if(!isMine) throw new UnauthorizedException("본인의 일기가 아닙니다.");

        diaryService.delete(id);

        return new MessageResponseDto("삭제 완료");

    }


    @Data
    static class CreateDiaryRequestDto {

        private Weather weather;

        private boolean secret;

        @NotEmpty
        private String backgroundColor;

        @NotEmpty
        private String title;

        @NotEmpty
        private String content;
    }

    @Data
    @AllArgsConstructor
    static class CreateDiaryResponseDto {
        private String id;

        private LocalDateTime regTime;
    }

    @Data
    @AllArgsConstructor
    static class ReadDiaryResponseDto {
        private String id;

        private String uid;

        private String userName;

        private LocalDateTime regTime;

        private Weather weather;

        private boolean secret;

        private String backgroundColor;

        private String title;

        private String content;

        public ReadDiaryResponseDto(Diary diary) {

            this.id = diary.getId();
            this.uid = diary.getUser().getUid();
            this.userName = diary.getUser().getName();
            this.regTime = diary.getRegTime();
            this.weather = diary.getWeather();
            this.secret = diary.isSecret();
            this.backgroundColor = diary.getBackgroundColor();
            this.title = diary.getTitle();
            this.content = diary.getContent();

        }
    }

    @Data
    static class UpdateDiaryRequestDto {

        private Weather weather;

        private boolean secret;

        @NotEmpty
        private String backgroundColor;

        @NotEmpty
        private String title;

        @NotEmpty
        private String content;
    }


}
