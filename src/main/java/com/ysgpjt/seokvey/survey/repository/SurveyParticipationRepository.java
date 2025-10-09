package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.entity.Survey;
import com.ysgpjt.seokvey.survey.entity.SurveyParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyParticipationRepository extends JpaRepository<SurveyParticipation, Long> {
    Optional<SurveyParticipation> findBySurvey(Survey survey);
    Optional<SurveyParticipation> findBySurveyAndAnonymousToken(Survey survey, String anonymousToken);
    Optional<SurveyParticipation> findBySurveyAndIpAddress(Survey survey, String ipAddress);
    Optional<SurveyParticipation> findBySurveyAndUserId(Survey survey, String userId);

}
