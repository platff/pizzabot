package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException {

        TelegramBotsApi botAPI = new TelegramBotsApi(DefaultBotSession.class);

        try {
            botAPI.registerBot(new PizzaBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}