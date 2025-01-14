package com.site.closedtrivia.answer;
import java.util.UUID;

public record AnswerDTO(
    UUID questionId,
    UUID triviaId,
    Answer userAnswer 
) {}
