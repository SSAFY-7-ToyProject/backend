package com.teamauc.diary.domain;

import com.teamauc.diary.util.SHA256;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
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

        @JoinColumn (name = "user_uid")
        @ManyToOne (fetch= FetchType.LAZY)
        @Setter (AccessLevel.NONE)
        @NotNull (message = "존재하지 않는 사용자 입니다")
        private User user;

        @Setter (AccessLevel.NONE)
        private LocalDateTime regTime;

        @Enumerated (EnumType.STRING)
        private Weather weather;

        private boolean secret;

        private String backgroundColor;

        private String title;

        private String content;

        @OneToMany(mappedBy = "diary")
        private List<Reply> replies = new ArrayList<>();

        // 생성 메소드 //

        public static Diary createDiary(User user, Weather weather, LocalDateTime regTime, boolean secret, String backgroundColor, String title, String content){
            Diary diary = new Diary();

            String diaryId = null;
            try {
                diaryId = SHA256.encrypt(user.getUid()+String.valueOf(LocalDateTime.now()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            diary.id = diaryId;
            diary.user = user;
            diary.regTime = regTime;
            diary.weather = weather;
            diary.secret = secret;
            diary.backgroundColor = backgroundColor;
            diary.title = title;
            diary.content = content;

            return diary;
        }

}
