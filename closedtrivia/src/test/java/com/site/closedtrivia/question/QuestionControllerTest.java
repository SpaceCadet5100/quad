package com.site.closedtrivia.question;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.site.closedtrivia.answer.*;
import com.site.closedtrivia.question.*;
import com.site.closedtrivia.quiz.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import reactor.core.publisher.Flux;
import java.util.UUID;
import java.util.List;

public class QuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizClient quizClient;

    @Mock
    private QuestionDTOToQuestionRecordTransformer questionDTOToQuestionRecordTransformer;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();
    }

    @Test
    void testGetQuestionsWithoutAnswer_Success() throws Exception {
	UUID realUuid = UUID.randomUUID();
	Answer answer = new Answer("answer");
	Question question = new Question(realUuid, realUuid, "", "", "", List.of(answer) , answer);
	Question questionNoAnswer = new Question(realUuid, realUuid, "", "", "", List.of(answer) , new Answer("GOOD LUCK"));
	QuestionDTO questionDTO = new QuestionDTO("", "", "", "", "", List.of(""));

        UUID triviaId = UUID.randomUUID();
        List<QuestionDTO> mockData = List.of(questionDTO);
        when(quizClient.findAll()).thenReturn(Flux.fromIterable(mockData));

        List<Question> transformedData = List.of(question);
        when(questionDTOToQuestionRecordTransformer.bulkTransform(mockData)).thenReturn(transformedData);

        doNothing().when(questionRepository).addBulk(transformedData);

        List<Question> strippedData = List.of(questionNoAnswer);
        when(questionRepository.getQuestionsWithoutAnswer(triviaId)).thenReturn(strippedData);

        mockMvc.perform(get("/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetQuestionsWithoutAnswer_NoDataFromQuizClient_ShouldThrowException() throws Exception {
        when(quizClient.findAll()).thenReturn(Flux.empty());

        mockMvc.perform(get("/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetQuestionsWithoutAnswer_QuizClientThrowsError_ShouldThrowException() throws Exception {
        when(quizClient.findAll()).thenThrow(new RuntimeException("Quiz client error"));

        mockMvc.perform(get("/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetQuestionsWithoutAnswer_TransformerThrowsException_ShouldThrowException() throws Exception {
	QuestionDTO questionDTO = new QuestionDTO("", "", "", "", "", List.of(""));

        List<QuestionDTO> mockData = List.of(questionDTO);
        when(quizClient.findAll()).thenReturn(Flux.fromIterable(mockData));

        when(questionDTOToQuestionRecordTransformer.bulkTransform(mockData))
                .thenThrow(new RuntimeException("Transformation error"));

        mockMvc.perform(get("/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetQuestionsWithoutAnswer_RepositoryThrowsException_ShouldThrowException() throws Exception {
	QuestionDTO questionDTO = new QuestionDTO("", "", "", "", "", List.of(""));
	UUID realUuid = UUID.randomUUID();
	Question question = new Question(realUuid, realUuid, "", "", "", List.of() , null);

        List<QuestionDTO> mockData = List.of(questionDTO);
        when(quizClient.findAll()).thenReturn(Flux.fromIterable(mockData));

        List<Question> transformedData = List.of(question);
        when(questionDTOToQuestionRecordTransformer.bulkTransform(mockData)).thenReturn(transformedData);

        doThrow(new RuntimeException("Repository error")).when(questionRepository).addBulk(transformedData);

        mockMvc.perform(get("/questions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
