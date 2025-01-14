package com.site.closedtrivia.answer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.site.closedtrivia.question.Question;
import com.site.closedtrivia.question.QuestionRepository;
import com.site.closedtrivia.question.QuestionValidatorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnswerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionValidatorService questionValidatorService;

    @InjectMocks
    private AnswerController answerController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(answerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void InputDataIsEmpty_ShouldReturnBadRequest() throws Exception {
        List<AnswerDTO> emptyInput = new ArrayList<>();

        mockMvc.perform(post("/checkanswers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void FirstTriviaIdIsNull_ShouldReturnBadRequest() throws Exception {
	UUID nullUuid = null;
	UUID realUuid = UUID.randomUUID();
	Answer answer = new Answer("answer");

	AnswerDTO answerDTO = new AnswerDTO(realUuid,nullUuid, answer);

        List<AnswerDTO> inputData = List.of(answerDTO);

        mockMvc.perform(post("/checkanswers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void NoQuestionsFound_ShouldReturnBadRequest() throws Exception {
	UUID realUuid = UUID.randomUUID();
	Answer answer = new Answer("answer");
	AnswerDTO answerDTO = new AnswerDTO(realUuid, realUuid, answer);
        List<AnswerDTO> inputData = List.of(answerDTO);

        when(questionRepository.findByQuizUUID(realUuid)).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/checkanswers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputData)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ValidInput_ShouldReturnOk() throws Exception {
	UUID realUuid = UUID.randomUUID();
	Answer answer = new Answer("answer");
	AnswerDTO answerDTO = new AnswerDTO(realUuid, realUuid, answer);
        List<AnswerDTO> inputData = List.of(answerDTO);

	AnswerResponseDTO answerResponseDTO = new AnswerResponseDTO(realUuid, realUuid, answer, answer);

	Question question = new Question(realUuid, realUuid, "", "", "", List.of(answer) , answer);

        List<Question> mockQuestions = List.of(question);
        List<AnswerResponseDTO> mockCheckedList = List.of(answerResponseDTO);

        when(questionRepository.findByQuizUUID(realUuid)).thenReturn(mockQuestions);
        when(questionValidatorService.batchValidate(inputData, mockQuestions)).thenReturn(mockCheckedList);

        mockMvc.perform(post("/checkanswers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputData)))
                .andExpect(status().isOk());
    }
}
