package com.ysgpjt.seokvey.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // 설문
    ALREADY_SURVEY_PARTICIPATION(HttpStatus.BAD_REQUEST,"이미 설문에 참여한 사람이 있습니다."),
    NOT_FOUND_SURVEY(HttpStatus.BAD_REQUEST,"존재하지 않는 설문입니다."),

    // 문항
    NOT_ENOUGH_QUESTION(HttpStatus.BAD_REQUEST,"문항이 없습니다."),
    NO_RESPONSE_QUESTION(HttpStatus.BAD_REQUEST,"응답하지 않은 문항이 있거나 중복된 문항이 있습니다."),
    INVALID_QUESTION(HttpStatus.BAD_REQUEST,"유효하지 않은 문항이 포함되어 있습니다."),

    // 옵션
    NOT_ENOUGH_OPTION(HttpStatus.BAD_REQUEST,"문항 옵션이 없거나 부족합니다."),
    NOT_CHOOSE_OPTION(HttpStatus.BAD_REQUEST,"문항에 대한 옵션이 선택되지 않았습니다."),
    ONE_CHOOSE_OPTION(HttpStatus.BAD_REQUEST,"단일 선택 문항은 옵션 1개만 선택해야 합니다."),
    INVALID_OPTION(HttpStatus.BAD_REQUEST,"유효하지 않는 옵션입니다."),


    // 사용자
    ALREADY_SURVEY_ANONYMOUS_TOKEN(HttpStatus.BAD_REQUEST,"이미 설문한 비회원 토큰입니다."),
    ALREADY_SURVEY_IPADDRESS(HttpStatus.BAD_REQUEST,"이미 설문한 IP 주소입니다."),
    ALREADY_SURVEY_CONSUMER(HttpStatus.BAD_REQUEST,"이미 설문한 사용자입니다.")


    ;

    private final HttpStatus httpStatus;
    private final String message;
}
