package com.site.closedtrivia.answer;
import java.util.UUID;


public record AnswerResponseDTO(
    UUID questionId,
    UUID triviaId,
    Answer correctAnswer,
    Answer userAnswer 
) {}
