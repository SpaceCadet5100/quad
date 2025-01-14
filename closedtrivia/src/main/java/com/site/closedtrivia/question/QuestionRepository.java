package com.site.closedtrivia.question;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import com.site.closedtrivia.answer.*;


import java.util.UUID;
import java.util.stream.Collectors;  


@Repository
public class QuestionRepository{

	private List<Question> questions = new ArrayList<Question>();

	public QuestionRepository(){
	}

	public void add(Question question){
		this.questions.add(question);
	}

	public void addBulk(List<Question> questions){
		this.questions.addAll(questions);
	}

	public List<Question> findByQuizUUID(UUID uuid){
		return this.questions.stream()
			.filter(question -> question.triviaId().equals(uuid))
			.collect(Collectors.toList());

	}

	public List<Question> getQuestionsWithoutAnswer(UUID uuid){
		List<Question> withAnswers = findByQuizUUID(uuid);

		 return withAnswers.stream()
		.map(question -> new Question(
		    question.questionId(),
		    question.triviaId(),
		    question.category(),
		    question.difficulty(),
		    question.questionText(),
		    question.answers(),      
		    new Answer("GOOD LUCK")
		))
		.collect(Collectors.toList()); 

	}
}
