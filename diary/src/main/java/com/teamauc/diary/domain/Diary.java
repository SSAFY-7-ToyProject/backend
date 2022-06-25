package com.teamauc.diary.domain;

import lombok.Getter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter

public class Diary {

        @Column (name= "diary_id")
        @Id
        private String id;

        @JoinColumn (name = "user_email")
        @ManyToOne (fetch= FetchType.LAZY)
        private User user;

        private LocalDateTime regTime;

        @Enumerated (EnumType.STRING)
        private Weather weather;

        private boolean secret;

        private String title;

        private String content;

        @OneToMany(mappedBy = "diary")
        private List<Reply> replies = new ArrayList<>();

        // 생성 메소드 //

        public static Diary createDiary(String id, User user, Weather weather, LocalDateTime regTime, boolean secret, String title, String content){
            Diary diary = new Diary();

            diary.id = id;
            diary.user = user;
            diary.regTime = regTime;
            diary.weather = weather;
            diary.secret = secret;
            diary.title = title;
            diary.content = content;

            return diary;
        }

}
