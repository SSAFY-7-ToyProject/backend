package com.teamauc.diary.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter

public class Diary {

        @Column (name= "diary_id")
        @Id
        @Setter (AccessLevel.NONE)
        private String id;

        @JoinColumn (name = "user_email")
        @ManyToOne (fetch= FetchType.LAZY)
        @Setter (AccessLevel.NONE)
        private User user;

        @Setter (AccessLevel.NONE)
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
