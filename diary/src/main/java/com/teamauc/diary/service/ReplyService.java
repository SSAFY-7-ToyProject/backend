package com.teamauc.diary.service;

import com.teamauc.diary.domain.Diary;
import com.teamauc.diary.domain.Reply;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.exception.InvalidApproachException;
import com.teamauc.diary.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;


    public List<Reply> findByUserEmail (String email) {

        List <Reply> replies = replyRepository.findByUserEmail(email);

        return replies;
    }

    public List<Reply> findByDiaryId (String id) {

        List <Reply> replies = replyRepository.findByDiaryId(id);

        return replies;
    }

    public Reply findById (Long id) {

        Reply reply = replyRepository.findById(id);

        return reply;
    }

    @Transactional
    public Long create(Reply reply){

        replyRepository.create(reply);

        return reply.getId();
    }

    @Transactional
    public Reply update(Long id, boolean secret, String content){

        Reply reply = replyRepository.findById(id);

        if (reply == null) {
            throw new InvalidApproachException("존재하지 않는 댓글입니다.");
        }

        else {
            reply.setSecret(secret);
            reply.setContent(content);

            return reply;
        }
    }

    @Transactional
    public void delete (Long id) {

        replyRepository.delete(id);

    }

}
