package com.site.closedtrivia.question;
import com.site.closedtrivia.answer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuestionValidatorServiceTest {

    private QuestionValidatorService questionValidatorService;

    @BeforeEach
    void setUp() {
        questionValidatorService = new QuestionValidatorService();
    }

    @Test
    void testBatchValidate_SuccessfulValidation() {
        UUID triviaId = UUID.randomUUID();
        UUID questionId1 = UUID.randomUUID();
        UUID questionId2 = UUID.randomUUID();

        List<AnswerDTO> userInput = List.of(
                new AnswerDTO(questionId1, triviaId, new Answer("H2O")),
                new AnswerDTO(questionId2, triviaId, new Answer("6"))
        );

        List<Question> questions = List.of(
                new Question(
                        questionId1,
                        triviaId,
                        "Science",
                        "Medium",
                        "What is the chemical symbol for water?",
                        List.of(new Answer("H2O"), new Answer("O2"), new Answer("CO2")),
                        new Answer("H2O")
                ),
                new Question(
                        questionId2,
                        triviaId,
                        "Math",
                        "Easy",
                        "What is 2+2*2?",
                        List.of(new Answer("6"), new Answer("4"), new Answer("8")),
                        new Answer("6")
                )
        );

        List<AnswerResponseDTO> result = questionValidatorService.batchValidate(userInput, questions);

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "There should be two validated answers");

        AnswerResponseDTO response1 = result.get(0);
        assertEquals(questionId1, response1.questionId());
        assertEquals(triviaId, response1.triviaId());
        assertEquals(new Answer("H2O"), response1.correctAnswer());
        assertEquals(new Answer("H2O"), response1.userAnswer());

        AnswerResponseDTO response2 = result.get(1);
        assertEquals(questionId2, response2.questionId());
        assertEquals(triviaId, response2.triviaId());
        assertEquals(new Answer("6"), response2.correctAnswer());
        assertEquals(new Answer("6"), response2.userAnswer());
    }

    @Test
    void testBatchValidate_InputSizeMismatch() {
        UUID triviaId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();

        List<AnswerDTO> userInput = List.of(
                new AnswerDTO(questionId, triviaId, new Answer("H2O"))
        );

        List<Question> questions = List.of(
                new Question(
                        questionId,
                        triviaId,
                        "Science",
                        "Medium",
                        "What is the chemical symbol for water?",
                        List.of(new Answer("H2O"), new Answer("O2"), new Answer("CO2")),
                        new Answer("H2O")
                ),
                new Question(
                        UUID.randomUUID(),
                        triviaId,
                        "Math",
                        "Easy",
                        "What is 2+2*2?",
                        List.of(new Answer("6"), new Answer("4"), new Answer("8")),
                        new Answer("6")
                )
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> questionValidatorService.batchValidate(userInput, questions)
        );

        assertEquals("The size of user input and questions must match.", exception.getMessage());
    }

    @Test
    void testBatchValidate_MissingCorrectAnswer() {
        UUID triviaId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();

        List<AnswerDTO> userInput = List.of(
                new AnswerDTO(questionId, triviaId, new Answer("H2O"))
        );

        List<Question> questions = List.of(
                new Question(
                        questionId,
                        triviaId,
                        "Science",
                        "Medium",
                        "What is the chemical symbol for water?",
                        List.of(new Answer("O2"), new Answer("CO2")), // Missing correct answer "H2O"
                        new Answer("H2O")
                )
        );

        QuestionValidatorService.IncorrectAnswerException exception = assertThrows(
                QuestionValidatorService.IncorrectAnswerException.class,
                () -> questionValidatorService.batchValidate(userInput, questions)
        );

        assertEquals("Correct answer not found for question ID " + questionId, exception.getMessage());
    }
}
