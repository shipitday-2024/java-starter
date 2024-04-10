package io.github.adamw7.orchestrator.ai.llama.connectivity.model;

public record OpenLlamaRequestBody(String prompt, double temperature, int n_predict) {
}
