package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.Reply;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.dto.MessageResponseDto;
import com.teamauc.diary.dto.ResultDto;
import com.teamauc.diary.service.DiaryService;
import com.teamauc.diary.service.ReplyService;
import com.teamauc.diary.service.UserService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final UserService userService;
    private final DiaryService diaryService;
    private final ReplyService replyService;


    @PostMapping
    public CreateReplyResponseDto createReply (@RequestBody CreateReplyRequestDto request) {

        Diary diary = diaryService.findByDiaryId(request.getDiaryId());
        User user = userService.SearchUserById(request.getUid());

        Reply reply = Reply.createReply(diary,user,LocalDateTime.now(),request.isSecret(), request.getContent());

        replyService.create(reply);

        return new CreateReplyResponseDto(reply.getId());

    }

    @GetMapping("/user/{uid}")
    public ResultDto readReplyByUserId (@PathVariable ("uid") String uid){

        List<ReadReplyResponseDto> replies = replyService.findByUserId(uid)
                .stream()
                .map(reply -> new ReadReplyResponseDto(reply))
                .collect(Collectors.toList());

        return new ResultDto(replies);

    }

    @GetMapping("/diary/{id}")
    public ResultDto readReplyByDiaryId (@PathVariable ("id") String id){

        List<ReadReplyResponseDto> replies = replyService.findByDiaryId(id)
                .stream()
                .map(reply -> new ReadReplyResponseDto(reply))
                .collect(Collectors.toList());

        return new ResultDto(replies);

    }

    @GetMapping("/{id}")
    public ResultDto readReplyById(@PathVariable ("id") Long id){

        Reply reply = replyService.findById(id);

        ReadReplyResponseDto replydto = new ReadReplyResponseDto(reply);

        return new ResultDto(replydto);
    }

    @PutMapping("/{id}")
    public MessageResponseDto updateReply(@PathVariable ("id") Long id,@RequestBody UpdateReplyRequestDto request){

        replyService.update(id,request.isSecret(), request.getContent());

        return new MessageResponseDto("댓글 수정 완료.");
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto deleteReply(@PathVariable ("id") Long id){

        replyService.delete(id);

        return new MessageResponseDto("댓글 삭제 완료.");
    }



    @Data
    static class CreateReplyRequestDto{

        private String diaryId;

        private String uid;

        private boolean secret;

        private String content;
    }

    @Data
    static class UpdateReplyRequestDto{

        private boolean secret;

        private String content;
    }

    @Data
    @AllArgsConstructor
    static class CreateReplyResponseDto{

        private Long id;
    }

    @Data
    @AllArgsConstructor
    static class ReadReplyResponseDto{

        private Long id;

        private String diaryId;

        private String uid;

        private String userName;

        private boolean secret;

        private String content;

        public ReadReplyResponseDto(Reply reply) {
            this.id = reply.getId();
            this.diaryId = reply.getDiary().getId();
            this.uid = reply.getUser().getUid();
            this.userName = reply.getUser().getName();
            this.secret = reply.isSecret();
            this.content = reply.getContent();
        }

    }

}
