package com.site.closedtrivia.question;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import com.site.closedtrivia.answer.*;


@Service
public class QuestionValidatorService{

    public List<AnswerResponseDTO> batchValidate(List<AnswerDTO> userInput, List<Question> questions) {

        if (userInput.size() != questions.size()) {
            throw new IllegalArgumentException("The size of user input and questions must match.");
        }

        Map<UUID, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::questionId, question -> question));

        return userInput.stream()
                .map(response -> validateResponse(response, questionMap))
                .collect(Collectors.toList());
    }

    private AnswerResponseDTO validateResponse(AnswerDTO response, Map<UUID, Question> questionMap) {

        Question question = questionMap.get(response.questionId());
        if (question == null) {
            throw new QuestionNotFoundException("Question with ID " + response.questionId() + " not found.");
        }

        Answer correctAnswer = question.answers().stream()
                .filter(answer -> question.correctAnswer().equals(answer))
                .findFirst()
                .orElseThrow(() -> new IncorrectAnswerException("Correct answer not found for question ID " + question.questionId()));

        return new AnswerResponseDTO(
                response.questionId(),
                response.triviaId(),
                correctAnswer,
                response.userAnswer()
        );
    }

    public class QuestionNotFoundException extends RuntimeException {
	    public QuestionNotFoundException(String message) {
		super(message);
	    }
    }

    public class IncorrectAnswerException extends RuntimeException {
	    public IncorrectAnswerException(String message) {
		super(message);
	    }
    }
}
