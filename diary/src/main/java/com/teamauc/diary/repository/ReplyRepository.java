package com.teamauc.diary.repository;

import com.teamauc.diary.domain.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {

    private final EntityManager em;

    public Long create (Reply reply){

        em.persist(reply);

        return reply.getId();
    }

    public List<Reply> findByUserEmail (String email) {

        List<Reply> replies = em.createQuery("SELECT r FROM Reply r WHERE r.user.email = :email",Reply.class)
                .setParameter("email",email)
                .getResultList();

        return replies;
    }

    public List<Reply> findByUserId(String uid) {

        List<Reply> replies = em.createQuery("SELECT r FROM Reply r WHERE r.user.uid = :uid",Reply.class)
                .setParameter("uid",uid)
                .getResultList();

        return replies;
    }

    public List<Reply> findByDiaryId (String id) {
        List<Reply> replies = em.createQuery("SELECT r FROM Reply r WHERE r.diary.id = :diaryId",Reply.class)
                .setParameter("diaryId",id)
                .getResultList();

        return replies;
    }


    public Reply findById (Long id){

        return em.find(Reply.class,id);
    }

    public void delete (Long id){

        em.remove(findById(id));

    }



}
