package com.ysgpjt.seokvey.survey.repository;

import com.ysgpjt.seokvey.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByEndDtBeforeAndResultGenerated(LocalDateTime now, Boolean resultGenerated);

}
