package com.ysgpjt.seokvey.survey.dto;

import com.ysgpjt.seokvey.type.SeletionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyParticipationQuery {

    private Long surveyParticipationId;         // 설문 참여 고유값
    private Long surveyId;                      // 설문 고유값
    private String userId;                      // 사용자 아이디
    private LocalDateTime surveyDt;             // 설문 참여 시각
    private Long questionId;                    // 문항 고유값
    private String questionContent;             // 문항 내용
    private Long questionOptionId;              // 문항 옵션 고유값
    private String questionOptionContent;       // 문항 옵션 내용


}
