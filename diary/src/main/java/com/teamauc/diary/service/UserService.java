package com.teamauc.diary.service;

import com.teamauc.diary.domain.Birth;
import com.teamauc.diary.domain.Gender;
import com.teamauc.diary.domain.User;
import com.teamauc.diary.exception.InvalidApproachException;
import com.teamauc.diary.exception.LoginException;
import com.teamauc.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new LoginException("이메일과 비밀번호를 확인해주세요1");
        }
        else {
            if(!password.equals(user.getPassword())){

                throw new LoginException("이메일과 비밀번호를 확인해주세요2");
            }
            else {
                return user;
            }
        }
    }

    public User SearchUserByEmail(String email) {

        return userRepository.findByEmail(email);

    }

    public User SearchUserById(String uid) {
        return userRepository.findById(uid);
    }

    public boolean checkEmailDuplicated(String email){           //중복 테스트
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public void update(String uid, String name, Birth birth, Gender gender,String phonenumber) {

        User user = userRepository.findById(uid);

        if( user == null ){
            throw new InvalidApproachException("등록되지 않은 사용자입니다.");
        }
        else {

            user.setName(name);
            user.setBirth(birth);
            user.setGender(gender);
            user.setPhoneNumber(phonenumber);


        }
    }

    @Transactional
    public String regist(User user) {

        userRepository.regist(user);

        return user.getUid();
    }

    @Transactional
    public void delete(String uid) {

        userRepository.delete(uid);
    }



}
