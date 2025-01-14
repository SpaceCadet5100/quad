package com.site.closedtrivia.question;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import com.site.closedtrivia.answer.*;

@Component
public class QuestionDTOToQuestionRecordTransformer{

	public List<Question> bulkTransform(List<QuestionDTO> input) {
	    List<Question> output = new ArrayList<>();

	    UUID questionId = UUID.randomUUID();

	    output = input.stream()
		    .map(e -> {
			UUID triviaId = UUID.randomUUID();
			List<Answer> answers = createAnswers(e);  

			Answer correctAnswer = new Answer(e.correct_answer());
			answers.add(correctAnswer);

			Question question = new Question(
				triviaId,
				questionId,
				e.category(),
				e.difficulty(),
				e.question(),
				answers,
				correctAnswer
			);

			return question;
		    })
		    .collect(Collectors.toList());

	    return output;
	}

	private List<Answer> createAnswers(QuestionDTO e) {
	    return e.incorrect_answers().stream()
		    .map(a -> new Answer(a))
		    .collect(Collectors.toList());
	}

}

