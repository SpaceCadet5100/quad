package com.site.closedtrivia.question;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.site.closedtrivia.quiz.*;
import java.util.UUID;

@RestController
public class QuestionController{

	private final QuestionRepository questionRepository; 
	private final QuizClient quizClient;
	private final QuestionDTOToQuestionRecordTransformer questionDTOToQuestionRecordTransformer;

	public QuestionController(QuestionRepository questionRepository, QuizClient quizClient, 
			QuestionDTOToQuestionRecordTransformer questionDTOToQuestionRecordTransformer){
		this.questionRepository= questionRepository; 
		this.quizClient = quizClient;
		this.questionDTOToQuestionRecordTransformer = questionDTOToQuestionRecordTransformer;
	}
 

	@GetMapping("/questions")
	@ResponseStatus(HttpStatus.OK)
	public List<Question> getQuestionsWithoutAnswer() {
	    try {
		List<QuestionDTO> data = quizClient.findAll()
			.collectList() 
			.block(); 

		if (data == null || data.isEmpty()) {
		    throw new InternalServerErrorException("No data found from the quiz client.");
		}

		List<Question> modifiedData = this.questionDTOToQuestionRecordTransformer.bulkTransform(data);
		questionRepository.addBulk(modifiedData);
		UUID uuid = modifiedData.get(0).triviaId();  
		
		List<Question> strippedData = this.questionRepository.getQuestionsWithoutAnswer(uuid);

		return strippedData;
	    } catch (Exception ex) {
		throw new InternalServerErrorException("Too many requests or another error occurred");
	    }
	}


	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public static class InternalServerErrorException extends RuntimeException {
	public InternalServerErrorException(String message) {
	    super(message);
		}
	}
}
