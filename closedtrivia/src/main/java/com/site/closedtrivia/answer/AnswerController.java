package com.site.closedtrivia.answer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import com.site.closedtrivia.question.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AnswerController{

	private final QuestionRepository questionRepository; 
	private final QuestionValidatorService questionValidatorService;

	public AnswerController(QuestionRepository questionRepository,
			QuestionValidatorService questionValidatorService) {
		        this.questionRepository= questionRepository; 
		        this.questionValidatorService = questionValidatorService;
	}
 
	@PostMapping("/checkanswers")
	public ResponseEntity<Map<String, Object>> postExample(@RequestBody List<AnswerDTO> inputData) {
	    Map<String, Object> response = new HashMap<>();

	    if (inputData.isEmpty()) {
		response.put("error", "Input data is empty.");
		return ResponseEntity.badRequest().body(response);
	    }

	    UUID firstUuid = inputData.get(0).triviaId();
	    if (firstUuid == null) {
		response.put("error", "The triviaId of the first answer is null.");
		return ResponseEntity.badRequest().body(response);
	    }

	    List<Question> questions = questionRepository.findByQuizUUID(firstUuid);
	    if (questions == null || questions.isEmpty()) {
		response.put("error", "No questions found for the provided trivia ID.");
		return ResponseEntity.badRequest().body(response);
	    }

	    List<AnswerResponseDTO> checkedList = questionValidatorService.batchValidate(inputData, questions);

	    response.put("input", checkedList);
	    return ResponseEntity.ok(response);
	}
}
