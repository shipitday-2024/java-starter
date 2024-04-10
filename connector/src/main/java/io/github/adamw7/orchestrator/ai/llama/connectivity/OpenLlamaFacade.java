package io.github.adamw7.orchestrator.ai.llama.connectivity;

import com.google.gson.Gson;
import io.github.adamw7.orchestrator.ai.llama.connectivity.model.OpenLlamaRequestBody;
import io.github.adamw7.orchestrator.ai.llama.connectivity.model.OpenLlamaResponse;
import io.github.adamw7.orchestrator.ai.llama.connectivity.model.OpenLlamaTokenResponse;
import io.github.adamw7.orchestrator.ai.llama.connectivity.model.OpenLlamaTokenizeRequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

final public class OpenLlamaFacade {
    private static final Logger log = LogManager.getLogger(OpenLlamaFacade.class.getName());
    public Optional<String> sendRequestToLlama(String prompt) {
        return sendRequestToLlama(prompt, OpenLlamaConfiguration.URL);
    }

    public Optional<String> sendRequestToLlama(String prompt, String url) {
        final OpenLlamaRequestBody openLlamaRequestBodyModel = new OpenLlamaRequestBody(prompt, 0.0, 512);
        HttpRequest.BodyPublisher postBody = getObjectAsAPIRequestBody(openLlamaRequestBodyModel);
        Optional<HttpResponse<String>> response = getStringHttpResponse(postBody, url);

        if (response.isPresent()) {
            final String body = response.get().body();
            final Gson gson = new Gson();
            // NOTE: the body, apart from model completion, contains a lot of metadata which could be used for diagnostic purposes
            final OpenLlamaResponse openLlamaResponse = gson.fromJson(body, OpenLlamaResponse.class);

            return Optional.of(openLlamaResponse.content());
        } else {
            return Optional.empty();
        }
    }

    public List<Integer> tokenizePrompt(String textToTokenize) {
        final OpenLlamaTokenizeRequestBody requestBody = new OpenLlamaTokenizeRequestBody(textToTokenize);
        HttpRequest.BodyPublisher postBody = getObjectAsAPIRequestBody(requestBody);
        Optional<HttpResponse<String>> response = getStringHttpResponse(postBody, OpenLlamaConfiguration.URL_TOKENIZE);

        if (response.isPresent()) {
            final String body = response.get().body();
            final Gson gson = new Gson();
            final Optional<OpenLlamaTokenResponse> openLlamaResponse = Optional.ofNullable(gson.fromJson(body, OpenLlamaTokenResponse.class));
            if (openLlamaResponse.isPresent()) {
                return openLlamaResponse.get().tokens;
            }
        }
            return Collections.emptyList();

    }

    private static Optional<HttpResponse<String>> getStringHttpResponse(HttpRequest.BodyPublisher postBody, String url) {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer %s".formatted(OpenLlamaConfiguration.API_KEY) )
                .timeout(Duration.ofMinutes(OpenLlamaConfiguration.HTTP_TIMEOUT))
                .POST(postBody)
                .build();

        Optional<HttpResponse<String>> response = Optional.empty();

        try {
        	HttpClient client = HttpClient.newHttpClient();
            final HttpResponse.BodyHandler<String> asString = HttpResponse.BodyHandlers.ofString();
            response = Optional.ofNullable(client.send(request, asString));
        } catch (IOException | InterruptedException ex) {
            log.error("AI ERROR - Exception during sent data", ex);
        }

        if (response.isEmpty()) {
            log.error("AI ERROR - Response from OpenLlama Instance was empty");
        }
        return response;
    }

    private static HttpRequest.BodyPublisher getObjectAsAPIRequestBody(Object prompt) {
        final Gson gson = new Gson();
        final String promptAsRequestBody = gson.toJson(prompt);

        return HttpRequest.BodyPublishers.ofString(promptAsRequestBody);
    }

}
