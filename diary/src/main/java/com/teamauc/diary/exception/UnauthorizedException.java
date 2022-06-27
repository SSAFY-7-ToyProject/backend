package com.teamauc.diary.exception;

public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = -2238030302650813813L;

    public UnauthorizedException() {
        super("권한이 없는 사용자입니다. 함부로 남의 사생활을 침해하지 마시길 바랍니다.");
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
