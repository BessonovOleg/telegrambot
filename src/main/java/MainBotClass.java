import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.toIntExact;

public class MainBotClass extends TelegramLongPollingBot{
    private String TOKEN = "631125685:AAEQon8INrqx1v_pkZlm_jz15c4S6vBQZXg";
    private String BOTNAME = "@T2studioBot";

    public String getBotToken() {
        return TOKEN;
    }


    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Contact contact = new Contact();

        if(update.hasCallbackQuery()){
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("btn_inline1")) {
                String answer = "Кнопка обработана";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        if(message != null) {
            if(message.hasText()) {
                if (message.getText().equals("/help")) {
                    StringBuilder msgBuilder = new StringBuilder();
                    msgBuilder.append("Привет, добро пожаловать!");
                    msgBuilder.append("\n");
                    msgBuilder.append("Вот что я могу:");
                    msgBuilder.append("\n");
                    msgBuilder.append("/members  - показываю всех пользователей");
                    msgBuilder.append("\n");
                    msgBuilder.append("/contacts - показываю всех пользователей и их адреса");
                    msgBuilder.append("\n");
                    msgBuilder.append("/user-data ID - показываю информацию по коду.");
                    msgBuilder.append("\n");
                    msgBuilder.append("/smile - показываю смайлы");
                    msgBuilder.append("\n");
                    msgBuilder.append("/myid - показываю Ваш ID");
                    msgBuilder.append("\n");
                    msgBuilder.append("/btn - показываю пример с кнопками");
                    msgBuilder.append("\n");
                    msgBuilder.append("/lb - показываю пример с кнопками в строке");
                    sendMsg(message, msgBuilder.toString());
                }

                if (message.getText().equals("/members")) {
                    sendMessageFromList(message, GoogleSheetWorker.getMembers());
                }

                if (message.getText().equals("/contacts")) {
                    sendMessageFromList(message, GoogleSheetWorker.getContacts());
                }

                if (message.getText().equals("/smile")) {
                    String answer = EmojiParser.parseToUnicode(":smile:\n :alien: \n :ua:");
                    sendMsg(message, answer);
                }

                if (message.getText().contains("/user-data")) {
                    String[] command = message.getText().split(" ");
                    if (command[1].length() > 0) {
                        sendMessageFromList(message, GoogleSheetWorker.getMemberByID(command[1]));
                    }
                }

                if (message.getText().contains("/myid")) {
                    sendMsg(message, "Your ID: " + message.getFrom().getId());
                }

                if (message.getText().contains("/btn")) {
                    sendMsgWithButton(message);
                }

                if (message.getText().contains("/lb")) {
                    sendMsgWithButtonInline(message);
                }
            }
        }
    }

    private void sendMsgWithButton(Message message){
        SendMessage sdMessage = new SendMessage();
        sdMessage.setChatId(message.getChatId().toString());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Button 1");
        row.add("Button 2");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Button 3");
        row.add("Button 4");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        sdMessage.setReplyMarkup(keyboardMarkup);
        sdMessage.setText("Пример");
        try {
            execute(sdMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendMsgWithButtonInline(Message message){
        SendMessage sdMessage = new SendMessage();
        sdMessage.setChatId(message.getChatId().toString());
        sdMessage.setText("Пример кнопки в строке");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowLine = new ArrayList<>();

        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText("Нажми " + EmojiParser.parseToUnicode(":smile:"));
        btn.setCallbackData("btn_inline1");
        rowLine.add(btn);

        rowsInline.add(rowLine);

        markupInLine.setKeyboard(rowsInline);
        sdMessage.setReplyMarkup(markupInLine);

        try {
            execute(sdMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendMessageFromList(Message message,List<String> messageData){
        if(messageData == null) return;
        StringBuilder msgBuilder = new StringBuilder();
        for (String member:messageData){
            msgBuilder.append(member).append("\n");
        }
        sendMsg(message,msgBuilder.toString());
    }

    private void sendMsg(Message message,String txtMsg){
        SendMessage sdMessage = new SendMessage();
        sdMessage.enableMarkdown(true);
        sdMessage.setChatId(message.getChatId().toString());
       // sdMessage.setReplyToMessageId(message.getMessageId());
        sdMessage.setText(txtMsg);
        try {
            execute(sdMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void sendMessageToUser(String userID,String txtMsg){
        SendMessage sdMessage = new SendMessage();
        sdMessage.setChatId(userID);
        sdMessage.setText(txtMsg);
        try {
            execute(sdMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return BOTNAME;
    }
}
