package it.game.service.llm;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class OllamaModelCreator extends LLMProvider {
    private static ChatModel instance;

    private final String baseURL = "http://localhost:11434";
    private final String modelName = "gemma:2b";

    @Override
    public synchronized ChatModel createModel() {
        if(instance == null){
            instance = OllamaChatModel.builder()
                    .baseUrl(baseURL)
                    .modelName(modelName)
                    .build();
        }
        return instance;
    }
}
