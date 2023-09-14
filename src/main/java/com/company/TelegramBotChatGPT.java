package com.company;

import com.company.chatgpt.ChatGPT;
import com.company.chatgpt.Conversation;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class TelegramBotChatGPT extends TelegramLongPollingBot {
    private String botUsername = "ChatGPTLiteVersionBot";
    private String botToken;
    final ChatGPT chatGPT = ChatGPT
            .builder()
            //.dataPath(Files.createTempDirectory("chatgpt")) // Persist the chat history to a data path
            .build();
    final Conversation conversation = chatGPT.newConversation();
    public TelegramBotChatGPT() {
        botToken = System.getenv("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            String question = update.getMessage().getText();
            try {

                String content = conversation.ask(question).content();
                message.setText(content);

                execute(message); // Call method to send the message

            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    conversation.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
