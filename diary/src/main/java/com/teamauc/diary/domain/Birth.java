package com.teamauc.diary.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@ToString
public class Birth {

    @Column (name = "birth_year")
    private Integer birthYear;

    @Column (name = "birth_month")
    private Integer birthMonth;

    @Column (name = "birth_day")
    private Integer birthDay;

    protected Birth(){};

    public Birth(Integer birthYear, Integer birthMonth, Integer birthDay) {
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
    }
}
