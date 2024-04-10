package io.github.adamw7.orchestrator.ai.llama;

public class OpenLlamaConnectivityException extends RuntimeException {
    public OpenLlamaConnectivityException(Exception e) {
        super(e);
    }
}
