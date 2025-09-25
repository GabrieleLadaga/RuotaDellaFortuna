package it.game.service.llm;

import dev.langchain4j.model.chat.ChatModel;

public abstract class LLMProvider {

    public abstract ChatModel createModel();

    public String getPuzzle(String category) {
        ChatModel provider = createModel();
        String prompt = buildPrompt(category);
        return provider.chat(prompt);
    }

    private String buildPrompt(String category) {
        return "Genera una frase per la categoria: " + category + ". Deve essere breve, non deve includere parole offensive o marchi.";
    }

}
