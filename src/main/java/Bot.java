import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try{
            telegramBotsApi.registerBot(new Bot());


        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());//which chat callback for id
        sendMessage.setReplyToMessageId(message.getMessageId());//what message callback
        sendMessage.setText(text);
        try{

            setButton(sendMessage);
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if(message !=null && message.hasText()){
            switch (message.getText()){
                case "/help":
                    sendMsg(message, "How can I help");
                    break;
                case "/setting":
                    sendMsg(message, "What do we do next?");
                    break;
                    default:
                        try {
                            sendMsg(message, Weather.getWeather(message.getText(), model));
                        }catch (IOException e){
                            sendMsg(message, "Not find!!!");
                        }

            }
        }
    }

    public void setButton(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowsList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowsList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowsList);
    }

    public String getBotUsername() {
        return "WeatherTryBot";
    }

    public String getBotToken() {
        return "658374445:AAECLkcKKXols_J5R3RnyA5Y7_elpAtE0A0";
    }
}
