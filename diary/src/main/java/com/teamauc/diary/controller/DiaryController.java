package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.domain.Weather;
import com.teamauc.diary.dto.MessageResponseDto;
import com.teamauc.diary.dto.ResultDto;
import com.teamauc.diary.service.DiaryService;
import com.teamauc.diary.service.UserService;
import com.teamauc.diary.util.SHA256;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;

    @PostMapping
    public CreateDiaryResponseDto createDiary (@RequestBody CreateDiaryRequestDto request){

        Diary diary = Diary.createDiary(
                userService.SearchUserById(request.uid),
                request.getWeather(),
                LocalDateTime.now(),
                request.isSecret(),
                request.getTitle(),
                request.getContent());

        diaryService.regist(diary);

        return new CreateDiaryResponseDto(diary.getId());
    }

    @GetMapping("/user/{uid}")
    public ResultDto readDiaryByEmail (@PathVariable ("uid") String uid){

        List<ReadDiaryResponseDto> diaryList = diaryService.findByUserId(uid).stream().map(diary -> new ReadDiaryResponseDto (
                diary.getId(),
                diary.getUser().getUid(),
                diary.getUser().getName(),
                diary.getRegTime(),
                diary.getWeather(),
                diary.isSecret(),
                diary.getTitle(),
                diary.getContent()
        )).collect(Collectors.toList());

        return new ResultDto(diaryList);

    }

    @GetMapping("/{id}")
    public ResultDto readDiaryById (@PathVariable ("id") String id){

        Diary diary = diaryService.findByDiaryId(id);

        return new ResultDto(new ReadDiaryResponseDto(
                diary.getId(),
                diary.getUser().getUid(),
                diary.getUser().getName(),
                diary.getRegTime(),
                diary.getWeather(),
                diary.isSecret(),
                diary.getTitle(),
                diary.getContent()
        ));

    }

    @PutMapping("/{diaryId}")
    public MessageResponseDto UpdateDiary (@PathVariable ("diaryId") String id, @RequestBody UpdateDiaryRequestDto request) {

        diaryService.update(id,request.getWeather(),request.isSecret(),request.getTitle(),request.getContent());

        return new MessageResponseDto ("수정 완료");
    }

    @DeleteMapping("{diaryId}")
    public MessageResponseDto DeleteDiary (@PathVariable ("diaryId") String id) {

        diaryService.delete(id);

        return new MessageResponseDto("삭제 완료");

    }



    @Data
    static class CreateDiaryRequestDto{

        private String uid;

        private Weather weather;

        private boolean secret;

        private String title;

        private String content;
    }

    @Data
    @AllArgsConstructor
    static class CreateDiaryResponseDto {
       private String id;
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

       private String title;

       private String content;
   }

   @Data
    static class UpdateDiaryRequestDto {

       private Weather weather;

       private boolean secret;

       private String title;

       private String content;
   }


}
