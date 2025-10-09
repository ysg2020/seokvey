package com.ysgpjt.seokvey.type;

public enum ActionType {
    VIEW_SURVEY, //→ 설문 페이지를 열어봄

    START_SURVEY,// → 설문을 시작함

    COMPLETE_SURVEY,// → 설문을 끝까지 완료함

    VIEW_QUESTION,// → 특정 질문을 확인함

    VOTE,// → 질문에 답변(옵션 선택)을 함

    CANCEL_VOTE,// → 선택을 취소함

    VIEW_RESULT,// → 설문/투표 결과를 조회함

    LOGIN,
    LOGOUT,

    ERROR// → 에러 발생 (예: 투표 실패)
}
