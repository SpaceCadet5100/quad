package com.site.closedtrivia.quiz;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import com.site.closedtrivia.question.*;

@Component
public class QuizClient {
	private final WebClient webClient;
        private static final String TRIVIA_API_URL = "https://opentdb.com/api.php?amount=10";

	public QuizClient(WebClient.Builder builder){
		this.webClient = builder.baseUrl(TRIVIA_API_URL).build();
	}

        public Flux<QuestionDTO> findAll() {
		return webClient.get()
			.retrieve()
			.bodyToMono(QuizDTO.class)
			.flatMapMany(response -> Flux.fromIterable(response.results()));
        }

}


