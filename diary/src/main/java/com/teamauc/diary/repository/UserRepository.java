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

     return user.getEmail();
 }



 public User findByEmail (String email) {                   // 회원가입시 아이디 존재 or email찾기

     return em.find(User.class,email);

 }

 public void delete (String email) {

     em.remove(findByEmail(email));

 }




}
