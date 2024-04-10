package io.github.adamw7.orchestrator.ai.llama.connectivity;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenLlamaFacadeTestsIT {
    @Test
    void shouldSendHTTPRequest() {
        // given
        var port = 8080;
        var endpoint = "/endpoint";
        var expectedOutput = "I am a java developer with 10 years of experience";
        WireMockServer wireMockServer = new WireMockServer(port);
        wireMockServer.start();
        stubFor(post(urlEqualTo(endpoint))
                .willReturn(aResponse().withBody(
                        """
                            {
                                "content" : "$output"
                            }
                        """.replace("$output", expectedOutput)
                )));

        // when
        String result = new OpenLlamaFacade()
                .sendRequestToLlama(UUID.randomUUID().toString(), "http://localhost:" + port + endpoint).get();

        // then
        verify(postRequestedFor(urlEqualTo(endpoint)));
        assertEquals(expectedOutput, result);
    }

    @Test
    void openLlamaInstanceIntegrationTest() {
        // given
        var expectedResult = "hello";
        var prompt = "[INST] Write a single word containing a string: \"%s\" [/INST]".formatted(expectedResult);

        // when
        var actualResult = new OpenLlamaFacade().sendRequestToLlama(prompt);

        // then
        assertThat(actualResult)
                .isPresent()
                .hasValueSatisfying(s -> assertThat(s).contains(expectedResult));
    }
}
