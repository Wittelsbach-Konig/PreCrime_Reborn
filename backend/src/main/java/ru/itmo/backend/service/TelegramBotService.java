package ru.itmo.backend.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.itmo.backend.config.TelegramBotConfig;

@Component
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramBotConfig telegramBotConfig;
    protected static final Logger logger = LogManager.getLogger();

    private static final String ERROR_TEXT = "Error occured ";
    private static final String HELP_TEXT = """
            This bot is created to demonstrate Power of PreCrime System.

            Authors: Kiryushin Vitaliy, Artem Khudyakov, Guskov Kirill

            You can execute commands from the main menu on the left or by typing a command:

            Type /start to see a welcome message

            Type /help to see this message again""";

    @Autowired
    public TelegramBotService(TelegramBotConfig telegramBotConfig) {
        this.telegramBotConfig = telegramBotConfig;
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getToken();
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
        logger.trace("Send message to chat: " + chatId);
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name +", nice to meet you!";
        sendMessage(chatId, answer);
        logger.trace("Replied to user " + name);
    }

    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            //System.out.printf(e.getMessage());
            logger.error(ERROR_TEXT + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }
    }
}
