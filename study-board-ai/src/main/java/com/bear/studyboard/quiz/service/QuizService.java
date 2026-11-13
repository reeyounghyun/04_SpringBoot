package com.bear.studyboard.quiz.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.post.entity.StudyPost;
import com.bear.studyboard.post.repository.StudyPostRepository;
import com.bear.studyboard.post.service.StudyPostService;
import com.bear.studyboard.quiz.dto.GeneratedQuizDTO;
import com.bear.studyboard.quiz.dto.QuizResponseDTO;
import com.bear.studyboard.quiz.entity.Quiz;
import com.bear.studyboard.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    // 게시글로 AI 퀴즈 만들기

    private final QuizRepository quizRepository;
    private final StudyPostRepository studyPostRepository;
    private final StudyPostService studyPostService;

    // 전달받은 생성된 퀴즈를 저장하는 역할!
    @Transactional
    public List<QuizResponseDTO> saveQuizzes(long id, List<GeneratedQuizDTO> validQuizzes) {
        //  한번 더 검증하는 구문
        //  AI 호출되는 동안 필기자료가 사려졌을 수도 있어서, 저장 직전에 다시 조회해야 한다.
        StudyPost post = studyPostService.findRequiredPost(id);
        LocalDateTime now = LocalDateTime.now();

        // 엔티티로 변경
        List<Quiz> quizzes = validQuizzes.stream()
                .map(validQuiz -> new Quiz(post, validQuiz.getQuestion(), validQuiz.getAnswer(), now))
                .toList();

        //saveAll() 목록형태로 넘겨줬을때
        // iterable한 객체 목록을 한번에 저장 하는 기능
        List<Quiz> savedQuizzes = quizRepository.saveAll(quizzes);

        // 응답 DTO로 변경하기

        /*
        List<QuizResponseDTO> quizResponseDTOList = savedQuizzes.stream()
                        .map(quiz -> QuizResponseDTO.from(quiz, post.getId()))
                        .toList();

                return quizResponseDTOList;
        */
        return savedQuizzes.stream()
                .map(quiz -> QuizResponseDTO.from(quiz, post.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public QuizResponseDTO findQuiz(long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUIZ_NOT_FOUND));

        StudyPost post = quiz.getStudyPost();

        return QuizResponseDTO.from(quiz, post.getId());


    }
}
