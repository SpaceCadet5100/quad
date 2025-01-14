package com.site.closedtrivia.question;
import java.util.List;
import java.util.UUID;
import com.site.closedtrivia.answer.*;

public record Question(
    UUID questionId,
    UUID triviaId,
    String category,
    String difficulty,
    String questionText,
    List<Answer> answers,
    Answer correctAnswer
) {}
