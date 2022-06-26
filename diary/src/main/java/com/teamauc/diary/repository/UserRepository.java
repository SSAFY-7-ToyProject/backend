package com.teamauc.diary.repository;

import com.teamauc.diary.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserRepository { // ->필요한 기능이 무엇이 있을까? Create Read Update Delete

 private final EntityManager em;

 public String regist(User user){

     em.persist(user);

     return user.getUid();
 }



 public User findByEmail (String email) {

     return em.find(User.class,email);

 }

    public User findById (String uid) {

        return em.find(User.class,uid);

    }

 public void delete (String uid) {

     em.remove(findById(uid));

 }




}
