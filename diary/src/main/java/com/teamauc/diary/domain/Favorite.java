package com.teamauc.diary.domain;

import com.teamauc.diary.util.SHA256;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Entity
@Getter
public class Favorite {

    @Id
    @Column(name="favorite_id")
    private String id;

    @JoinColumn(name = "user_uid")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @JoinColumn(name = "friend_uid")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User friend;

    @CreatedDate
    private LocalDateTime regTime;


    public static Favorite createFriend(User user, User friend) {

        Favorite favorite = new Favorite();
        String favoriteId = "";
        try {
            favoriteId = SHA256.encrypt(user.getUid()+String.valueOf(LocalDateTime.now()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        favorite.id = favoriteId;
        favorite.user = user;
        favorite.friend = friend;

        return favorite;
    }
}
