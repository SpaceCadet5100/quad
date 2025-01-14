package com.site.closedtrivia.question;
import java.util.List;

public record QuestionDTO(
    String category,
    String type,
    String difficulty,
    String question,
    String correct_answer,
    List<String> incorrect_answers
) {}

