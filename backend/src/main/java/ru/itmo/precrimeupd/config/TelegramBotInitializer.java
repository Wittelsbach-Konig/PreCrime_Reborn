package ru.itmo.precrimeupd.config;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.itmo.precrimeupd.service.TelegramBotService;

public class TelegramBotInitializer {
    TelegramBotService Bot;

    @Autowired
    public TelegramBotInitializer(TelegramBotService bot) {
        Bot = bot;
    }

    @EventListener({ContextRefreshedEvent.class})
     public void init() throws TelegramApiException {
         TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
         try {
             telegramBotsApi.registerBot(Bot);
         }
         catch (TelegramApiException e) {
             // log.error("Error occurred: " + e.getMessage());
             System.out.println(e.getMessage());
         }
     }
}
