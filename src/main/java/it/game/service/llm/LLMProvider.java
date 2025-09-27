package it.game.service.llm;

import dev.langchain4j.model.chat.ChatModel;

public abstract class LLMProvider {

    public abstract ChatModel createModel();

    public String getPuzzle(String category, int maxLength) {
        ChatModel provider = createModel();
        String prompt = buildPrompt(category, maxLength);
        return provider.chat(prompt);
    }

    private String buildPrompt(String category, int maxLength) {
        return String.format(
                "Genera una frase in italiano per la categoria: %s." +
                " La frase deve essere chiara, breve e avere al massimo: %d caratteri." +
                " Non deve includere parole offensive o marchi registrati.", category, maxLength);
    }

}
