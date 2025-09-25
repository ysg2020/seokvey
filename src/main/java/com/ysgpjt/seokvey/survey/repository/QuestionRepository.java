package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.entity.Question;
import com.ysgpjt.seokvey.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySurvey(Survey survey);

}
