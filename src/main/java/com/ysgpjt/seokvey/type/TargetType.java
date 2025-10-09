package com.ysgpjt.seokvey.type;

public enum TargetType {
    SURVEY,// → 설문 자체

    QUESTION,// → 설문 내 질문

    OPTION,// → 질문 내 옵션(선택지)

    USER,// → 다른 사용자(예: 친구 초대, 팔로우)

    SYSTEM// → 시스템 자체 이벤트
}
