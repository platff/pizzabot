package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PizzaBot extends TelegramLongPollingBot {

    private String botToken;
    private String botUsername;
    private String greetingMessageText;

    public PizzaBot() {

        FileInputStream propertiesFile;
        Properties properties = new Properties();

        try {
            propertiesFile = new FileInputStream("src\\main\\resources\\application.properties");
        } catch (IOException e) {
            System.err.println("properties file not found."); // TODO
            return;
        }

        try {
            properties.load(propertiesFile);
        } catch (IOException e) {
            System.err.println();// TODO
        }

        botToken            = properties.getProperty("bot.token");
        botUsername         = properties.getProperty("bot.username");
        greetingMessageText = properties.getProperty("greetingMessage");

    }
    @Override
    public String getBotUsername() {
        return botUsername;
    }
    @Override
    public String getBotToken() {
        return botToken;
    }

    public String getGreetingMessageText(){
        return greetingMessageText;
    }
    @Override
    public void onUpdateReceived(Update update) {
        String echoMessageText;

        if (!update.hasMessage()) {
            return;
        }

        String currentChatID        = update.getMessage().getChatId().toString();
        if(update.getMessage().hasText()) {
            echoMessageText = update.getMessage().getText();
        }
        else {
            echoMessageText = "Your message is emty. Sorry I can't reply to that.";
        }

        sendMessage(currentChatID, getGreetingMessageText());
        sendMessage(currentChatID, echoMessageText);
    }

    private void sendMessage(String chatID, String messageText){

        SendMessage message = new SendMessage();

        message.setChatId(chatID);
        message.setText(messageText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace(); // TODO
        }
    }
}
