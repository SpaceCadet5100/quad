package com.site.closedtrivia.question;

import static org.junit.jupiter.api.Assertions.*;

import com.site.closedtrivia.answer.Answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class QuestionDTOToQuestionRecordTransformerTest {

    private QuestionDTOToQuestionRecordTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new QuestionDTOToQuestionRecordTransformer();
    }

    @Test
    void testBulkTransform_ValidInput_ShouldReturnTransformedQuestions() {
        String category = "Science";
        String difficulty = "Medium";
        String type = "multiple";
        String questionText = "What is the chemical symbol for water?";
        String correctAnswer = "H2O";
        List<String> incorrectAnswers = List.of("O2", "CO2", "H2");

        QuestionDTO questionDTO = new QuestionDTO(
                category,
		type,
                difficulty,
                questionText,
                correctAnswer,
                incorrectAnswers
        );

        List<Question> result = transformer.bulkTransform(List.of(questionDTO));

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain one question");

        Question transformedQuestion = result.get(0);
        assertEquals(category, transformedQuestion.category(), "Category should match");
        assertEquals(difficulty, transformedQuestion.difficulty(), "Difficulty should match");
        assertEquals(questionText, transformedQuestion.questionText(), "Question text should match");
        assertNotNull(transformedQuestion.triviaId(), "Trivia ID should not be null");
        assertNotNull(transformedQuestion.questionId(), "Question ID should not be null");

        List<Answer> answers = transformedQuestion.answers();
        assertNotNull(answers, "Answers list should not be null");
        assertEquals(4, answers.size(), "Answers list should contain all answers");

        assertTrue(answers.stream().anyMatch(a -> a.text().equals(correctAnswer)), "Answers should include the correct answer");
        assertTrue(answers.stream().anyMatch(a -> incorrectAnswers.contains(a.text())), "Answers should include all incorrect answers");
        assertEquals(correctAnswer, transformedQuestion.correctAnswer().text(), "Correct answer should match");
    }

    @Test
    void testBulkTransform_EmptyInput_ShouldReturnEmptyList() {
        List<Question> result = transformer.bulkTransform(List.of());

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be an empty list");
    }

    @Test
    void testCreateAnswers_ShouldReturnTransformedAnswers() {
        String incorrectAnswer1 = "Option 1";
        String incorrectAnswer2 = "Option 2";
        String incorrectAnswer3 = "Option 3";

        QuestionDTO questionDTO = new QuestionDTO(
                "Category",
		"multiple",
                "Easy",
                "Sample Question?",
                "Correct Answer",
                List.of(incorrectAnswer1, incorrectAnswer2, incorrectAnswer3));


        List<Answer> answers = transformer.bulkTransform(List.of(questionDTO)).get(0).answers();

        assertNotNull(answers, "Answers list should not be null");
        assertEquals(4, answers.size(), "Answers list should contain all answers (correct + incorrect)");

        assertTrue(answers.stream().anyMatch(a -> a.text().equals("Correct Answer")), "Correct answer should be present");
        assertTrue(answers.stream().anyMatch(a -> a.text().equals(incorrectAnswer1)), "Incorrect answer 1 should be present");
        assertTrue(answers.stream().anyMatch(a -> a.text().equals(incorrectAnswer2)), "Incorrect answer 2 should be present");
        assertTrue(answers.stream().anyMatch(a -> a.text().equals(incorrectAnswer3)), "Incorrect answer 3 should be present");
    }
}

