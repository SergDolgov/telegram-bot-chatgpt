package com.company;

import com.company.chatgpt.ChatGPT;
import com.company.chatgpt.Conversation;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;

public class TelegramBotChatGPT extends TelegramLongPollingBot {
    final ChatGPT chatGPT = ChatGPT
            .builder()
            .dataPath(Files.createTempDirectory("chatgpt")) // Persist the chat history to a data path
            .build();
    final Conversation conversation = chatGPT.newConversation();
    public TelegramBotChatGPT() throws IOException {
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
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                try {
                    conversation.save();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "ChatGPTLiteVersionBot";
        //return "BanderogusSergGoItBot";
    }

    @Override
    public String getBotToken() {
        return "6617616396:AAHDJXbCTIn_GI9s1VkG3KlXYmKgFcxdjhY";
        //return "6166506459:AAGlClhbNtLSl_VJ6dY0x14wbIRMQfXEVTM";
    }

}
