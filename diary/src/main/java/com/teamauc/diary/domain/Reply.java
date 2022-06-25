package com.teamauc.diary.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter

public class Reply {

    @Id @GeneratedValue
    @Column (name = "reply_id")
    private int id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "diary_id")
    private Diary diary;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_email")
    private User user;

    private LocalDateTime regTime;

    private boolean secret;

    private String content;

    // 생성 메소드 //

    public static Reply createReply(int id, Diary diary, User user, LocalDateTime regTime, boolean secret, String content){
        Reply reply = new Reply();

        reply.id = id;
        reply.diary = diary;
        reply.user = user;
        reply.regTime = regTime;
        reply.secret = secret;
        reply.content = content;

        return reply;
    }

}
