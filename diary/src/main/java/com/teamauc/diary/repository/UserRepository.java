package com.teamauc.diary.repository;

import com.teamauc.diary.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository { // ->필요한 기능이 무엇이 있을까? Create Read Update Delete

    private final EntityManager em;

    public String regist(User user) {

        em.persist(user);

        return user.getUid();
    }


    public User findByEmail(String email) {
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.email = :email").setParameter("email", email).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(String uid) {

        return em.find(User.class, uid);

    }

    public List<User> searchUserByEmail(String word) {
        List<User> seachedUser = em.createQuery("SELECT u FROM User u WHERE u.name like :word").setParameter("word",word+"%").getResultList();
        return seachedUser;
    }

    public void delete(String uid) {

        em.remove(findById(uid));

    }


}
