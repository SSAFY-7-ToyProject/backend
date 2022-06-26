package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.domain.Weather;
import com.teamauc.diary.dto.MessageResponseDto;
import com.teamauc.diary.dto.ResultDto;
import com.teamauc.diary.service.DiaryService;
import com.teamauc.diary.service.UserService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
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

        String diaryid = String.valueOf(LocalDateTime.now()); // 나중에 해시값으로 바꿀것

        Diary diary = Diary.createDiary(
                diaryid,
                userService.SearchUserByEmail(request.email),
                request.getWeather(),
                LocalDateTime.now(),
                request.isSecret(),
                request.getTitle(),
                request.getContent());

        diaryService.regist(diary);

        return new CreateDiaryResponseDto(diary.getId());
    }

    @GetMapping("/user/{email}")
    public ResultDto readDiaryByEmail (@PathVariable ("email") String email){

        List<ReadDiaryResponseDto> diaryList = diaryService.findByUserEmail(email).stream().map(diary -> new ReadDiaryResponseDto (
                diary.getId(),
                diary.getUser().getEmail(),
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
                diary.getUser().getEmail(),
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

        private String email;

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

       private String email;

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
