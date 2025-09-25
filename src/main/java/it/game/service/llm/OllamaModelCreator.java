package it.game.service.llm;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class OllamaModelCreator extends LLMProvider {
    private final String baseURL = "http://localhost:11434";
    private final String modelName = "llama2";

    @Override
    public ChatModel createModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseURL)
                .modelName(modelName)
                .build();
    }
}
