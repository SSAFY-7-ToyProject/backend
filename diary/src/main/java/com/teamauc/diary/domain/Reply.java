package com.teamauc.diary.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter @Setter

public class Reply {

    @Id @GeneratedValue
    @Column (name = "reply_id")
    @Setter (AccessLevel.NONE)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "diary_id")
    @Setter (AccessLevel.NONE)
    private Diary diary;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_email")
    @Setter (AccessLevel.NONE)
    @NotNull (message = "존재하지 않는 사용자 입니다")
    private User user;

    @Setter (AccessLevel.NONE)
    private LocalDateTime regTime;

    private boolean secret;

    private String content;

    // 생성 메소드 //

    public static Reply createReply(Diary diary, User user, LocalDateTime regTime, boolean secret, String content){
        Reply reply = new Reply();

//        reply.id = id;
        reply.diary = diary;
        reply.user = user;
        reply.regTime = regTime;
        reply.secret = secret;
        reply.content = content;

        return reply;
    }

}
