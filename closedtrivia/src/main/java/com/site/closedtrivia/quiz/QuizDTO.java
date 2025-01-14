package com.site.closedtrivia.quiz;

import java.util.List;

import com.site.closedtrivia.question.*;

public record QuizDTO(
    int responseCode,
    List<QuestionDTO> results
) {}
