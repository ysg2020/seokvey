package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.entity.Question;
import com.ysgpjt.seokvey.survey.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByQuestion(Question question);

}
