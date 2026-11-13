package com.bear.studyboard.quiz.repository;

import com.bear.studyboard.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
