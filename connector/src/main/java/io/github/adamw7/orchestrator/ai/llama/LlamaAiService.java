package io.github.adamw7.orchestrator.ai.llama;

import java.util.List;
import java.util.Optional;

import io.github.adamw7.orchestrator.ai.llama.connectivity.OpenLlamaFacade;

public class LlamaAiService implements AiService {
    private final OpenLlamaFacade llamaFacade;

    public LlamaAiService(OpenLlamaFacade llamaFacade) {
        this.llamaFacade = llamaFacade;
    }

    @Override
    public Optional<String> generate(String prompt) {
        return llamaFacade.sendRequestToLlama(prompt);
    }

    public List<Integer> tokenize(String textToTokenize) {
        return llamaFacade.tokenizePrompt(textToTokenize);
    }
}
