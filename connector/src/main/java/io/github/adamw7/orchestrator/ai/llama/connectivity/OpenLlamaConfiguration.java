package io.github.adamw7.orchestrator.ai.llama.connectivity;

final class OpenLlamaConfiguration {
    public static final String API_KEY = "WDdOH68a2LRsFUJWgWaCBtVLoOR1jR6miKRA1lYC2Pm7m2KVqgChDkwKNR9eVTBf";
    private static final int PORT = 8080;
    private static final String HOST = "http://23.97.191.61";
    private static final String ENDPOINT = "/completion";
    static final String URL = HOST + ":" + PORT + ENDPOINT;
    private static final String ENDPOINT_TOKENIZE = "/tokenize";
    static final String URL_TOKENIZE = HOST + ":" + PORT + ENDPOINT_TOKENIZE;
    static final int HTTP_TIMEOUT = 10;
}
