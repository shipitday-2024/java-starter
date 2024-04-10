package io.github.adamw7.orchestrator.ai.llama;

import java.util.Optional;

public interface AiService {
    Optional<String> generate(String prompt);
}
