package com.teamauc.diary.controller;

import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.Reply;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.dto.MessageResponseDto;
import com.teamauc.diary.dto.ResultDto;
import com.teamauc.diary.exception.InvalidApproachException;
import com.teamauc.diary.exception.UnauthorizedException;
import com.teamauc.diary.service.DiaryService;
import com.teamauc.diary.service.JwtService;
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
    private final JwtService jwtService;


    @PostMapping
    public CreateReplyResponseDto createReply (@RequestBody CreateReplyRequestDto request) {

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();
        Diary diary = diaryService.findByDiaryId(request.getDiaryId());
        User user = userService.SearchUserById(jwtService.getUserId());

        boolean isMyDiary = currentUid.equals(diary.getUser().getUid());

        if(!isMyDiary&&diary.isSecret()) throw new UnauthorizedException("남의 비밀일기에 댓글을 달 수 없습니다.");

        Reply reply = Reply.createReply(diary,user,LocalDateTime.now(),request.isSecret(), request.getContent());

        replyService.create(reply);

        return new CreateReplyResponseDto(reply.getId());

    }

    @GetMapping("/user/{uid}")
    public ResultDto readReplyByUserId (@PathVariable ("uid") String uid){

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();

        boolean isMine = currentUid.equals(uid);

        if(!isMine) throw new UnauthorizedException();

        List<ReadReplyResponseDto> replies = replyService.findByUserId(uid)
                .stream()
                .map(reply -> new ReadReplyResponseDto(reply))
                .collect(Collectors.toList());

        return new ResultDto(replies);

    }

    @GetMapping("/diary/{id}")
    public ResultDto readReplyByDiaryId (@PathVariable ("id") String id){

        if (!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();
        Diary diary = diaryService.findByDiaryId(id);
        String diaryOwnerUid = diary.getUser().getUid();

        boolean isMine = diaryOwnerUid.equals(currentUid);

        if(diary.isSecret()&&!isMine) throw new UnauthorizedException("비밀 일기에 달린 댓글입니다.");

        List<ReadReplyResponseDto> replies = replyService.findByDiaryId(id)
                .stream()
                .map(reply -> {

                    if (!reply.isSecret()) return new ReadReplyResponseDto(reply);

                    boolean isQualifiedUser = (reply.getId().equals(currentUid))||isMine;

                    if (isQualifiedUser) {
                        return new ReadReplyResponseDto(reply);
                    }
                    else {
                        reply.setContent("비밀 댓글입니다.");
                        return new ReadReplyResponseDto(reply);
                    }


                })
                .collect(Collectors.toList());

        return new ResultDto(replies);

    }

    @GetMapping("/{id}")
    public ResultDto readReplyById(@PathVariable ("id") Long id){

        if(!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();
        Reply reply = replyService.findById(id);

        boolean isMine = currentUid.equals(reply.getUser().getUid());

        if(!isMine) throw new UnauthorizedException();


        ReadReplyResponseDto replydto = new ReadReplyResponseDto(reply);

        return new ResultDto(replydto);
    }

    @PutMapping("/{id}")
    public MessageResponseDto updateReply(@PathVariable ("id") Long id,@RequestBody UpdateReplyRequestDto request){

        if(!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();
        Reply reply = replyService.findById(id);

        boolean isMine = currentUid.equals(reply.getUser().getUid());

        if(!isMine) throw new UnauthorizedException();

        replyService.update(id,request.isSecret(), request.getContent());

        return new MessageResponseDto("댓글 수정 완료.");
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto deleteReply(@PathVariable ("id") Long id){

        if(!jwtService.isValidUser())
            throw new InvalidApproachException("사용자 인증 실패");

        String currentUid = jwtService.getUserId();
        Reply reply = replyService.findById(id);

        boolean isMine = currentUid.equals(reply.getUser().getUid());

        if(!isMine) throw new UnauthorizedException();

        replyService.delete(id);

        return new MessageResponseDto("댓글 삭제 완료.");
    }



    @Data
    static class CreateReplyRequestDto{

        private String diaryId;

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
