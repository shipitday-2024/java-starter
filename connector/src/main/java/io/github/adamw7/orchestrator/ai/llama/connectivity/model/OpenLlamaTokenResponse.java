package io.github.adamw7.orchestrator.ai.llama.connectivity.model;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class OpenLlamaTokenResponse {
    public List<Integer> tokens;
}
