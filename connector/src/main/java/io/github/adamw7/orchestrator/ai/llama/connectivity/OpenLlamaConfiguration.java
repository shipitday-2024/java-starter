package io.github.adamw7.orchestrator.ai.llama.connectivity;

final class OpenLlamaConfiguration {
    public static final String API_KEY = "aie93JaTv1GW1AP4IIUSqeecV22HgpcQ6WlgWNyfx2HflkY5hTw19JDbT90ViKcZaZ6lpjOo3YIGgpkG7Zb8jEKvdM5Ymnq9jPm79osLppCebwJ7WdWTwWq3Rf15NDxm";
    private static final int PORT = 8000;
    private static final String HOST = "http://20.86.28.184";
    private static final String ENDPOINT = "/completion";
    static final String URL = HOST + ":" + PORT + ENDPOINT;
    private static final String ENDPOINT_TOKENIZE = "/tokenize";
    static final String URL_TOKENIZE = HOST + ":" + PORT + ENDPOINT_TOKENIZE;
    static final int HTTP_TIMEOUT = 10;
}
