package com.site.closedtrivia.question;
import static org.junit.jupiter.api.Assertions.*;

import com.site.closedtrivia.answer.Answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class QuestionRepositoryTest {

    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        questionRepository = new QuestionRepository();
    }

    @Test
    void testAdd_ShouldAddQuestion() {
        UUID triviaId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        Question question = new Question(
                questionId,
                triviaId,
                "Science",
                "Medium",
                "What is the chemical symbol for water?",
                List.of(new Answer("H2O"), new Answer("CO2")),
                new Answer("H2O")
        );

        questionRepository.add(question);

        List<Question> result = questionRepository.findByQuizUUID(triviaId);
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "There should be one question");
        assertEquals(question, result.get(0), "The question should match the added question");
    }

    @Test
    void testAddBulk_ShouldAddMultipleQuestions() {
        UUID triviaId = UUID.randomUUID();
        List<Question> questions = List.of(
                new Question(
                        UUID.randomUUID(),
                        triviaId,
                        "Science",
                        "Easy",
                        "What is the speed of light?",
                        List.of(new Answer("3x10^8 m/s"), new Answer("1x10^8 m/s")),
                        new Answer("3x10^8 m/s")
                ),
                new Question(
                        UUID.randomUUID(),
                        triviaId,
                        "Math",
                        "Hard",
                        "What is 2+2*2?",
                        List.of(new Answer("6"), new Answer("8")),
                        new Answer("6")
                )
        );

        questionRepository.addBulk(questions);

        List<Question> result = questionRepository.findByQuizUUID(triviaId);
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "There should be two questions");
        assertTrue(result.containsAll(questions), "The repository should contain all added questions");
    }

    @Test
    void testFindByQuizUUID_ShouldReturnFilteredQuestions() {
        UUID triviaId1 = UUID.randomUUID();
        UUID triviaId2 = UUID.randomUUID();
        Question question1 = new Question(
                UUID.randomUUID(),
                triviaId1,
                "Science",
                "Easy",
                "What is the boiling point of water?",
                List.of(new Answer("100?C"), new Answer("90?C")),
                new Answer("100?C")
        );
        Question question2 = new Question(
                UUID.randomUUID(),
                triviaId2,
                "History",
                "Medium",
                "Who discovered America?",
                List.of(new Answer("Christopher Columbus"), new Answer("Vasco da Gama")),
                new Answer("Christopher Columbus")
        );

        questionRepository.add(question1);
        questionRepository.add(question2);

        List<Question> result = questionRepository.findByQuizUUID(triviaId1);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "There should be one question");
        assertEquals(question1, result.get(0), "The question should match the one with triviaId1");
    }

    @Test
    void testGetQuestionsWithoutAnswer_ShouldReturnQuestionsWithDefaultAnswer() {
        UUID triviaId = UUID.randomUUID();
        Question question = new Question(
                UUID.randomUUID(),
                triviaId,
                "Science",
                "Medium",
                "What is the chemical symbol for water?",
                List.of(new Answer("H2O"), new Answer("CO2")),
                new Answer("H2O")
        );

        questionRepository.add(question);

        List<Question> result = questionRepository.getQuestionsWithoutAnswer(triviaId);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "There should be one question");
        Question transformedQuestion = result.get(0);
        assertNotNull(transformedQuestion.correctAnswer(), "Correct answer should not be null");
        assertEquals("GOOD LUCK", transformedQuestion.correctAnswer().text(), "Correct answer should be 'GOOD LUCK'");
    }
}
