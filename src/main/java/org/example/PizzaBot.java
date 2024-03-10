package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class PizzaBot extends TelegramLongPollingBot {

    private String botToken;
    private String botUsername;
    private String greetingMessageText;
    private String emptyMessageText;
    private String waitingMessageText;
    private String replyMessageText;

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
        greetingMessageText = properties.getProperty("messages.greetingMessage");
        waitingMessageText  = properties.getProperty("messages.waitingMessage");
        emptyMessageText    = properties.getProperty("messages.emptyMessage");
        replyMessageText    = properties.getProperty("messages.replyMessage");

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

    public String getWaitingMessageText(){
        return waitingMessageText;
    }
    public String getEmptyMessageText(){
        return emptyMessageText;
    }
    public String getReplyMessageText(){
        return replyMessageText;
    }
    @Override
    public void onUpdateReceived(Update update) {
        String echoMessageText;

        if (!update.hasMessage()) {
            return;
        }

        Message receivedMessage = update.getMessage();

        String currentChatID = receivedMessage.getChatId().toString();

        if (receivedMessage.isCommand()) {
            sendMessage(currentChatID, getGreetingMessageText());
            sendMessage(currentChatID, getWaitingMessageText());
        }
        else if (receivedMessage.hasText()) {
            sendMessage(currentChatID, getReplyMessageText());
            sendButton(currentChatID, receivedMessage.getText());
        }
        else {
            sendMessage(currentChatID, getEmptyMessageText());
        }

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
    private void sendButton(String chatID, String messageText){
        var nextButton = InlineKeyboardButton.builder()
                .text("Next")
                .callbackData("next")
                .build();

        InlineKeyboardMarkup grid = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(nextButton))
                .build();

        SendMessage message = SendMessage.builder()
                .parseMode("HTML").text(messageText)
                .chatId(chatID)
                .replyMarkup(grid).build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace(); // TODO
        }

    }
}
