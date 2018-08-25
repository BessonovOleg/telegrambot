import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class MainBotClass extends TelegramLongPollingBot{
    private String TOKEN = "631125685:AAEQon8INrqx1v_pkZlm_jz15c4S6vBQZXg";
    private String BOTNAME = "@T2studioBot";

    public String getBotToken() {
        return TOKEN;
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Contact contact = new Contact();



        if(message != null && message.hasText()){
            if(message.getText().equals("/help")) {
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
                sendMsg(message, msgBuilder.toString());

            }

            if(message.getText().equals("/members")){
                sendMessageFromList(message,GoogleSheetWorker.getMembers());
            }

            if(message.getText().equals("/contacts")){
                sendMessageFromList(message,GoogleSheetWorker.getContacts());
            }

            if(message.getText().equals("/smile")){
                String answer = EmojiParser.parseToUnicode(":smile:\n :alien: \n :ua:");
                sendMsg(message, answer);
            }

            if(message.getText().contains("/user-data")){
                String[] command = message.getText().split(" ");
                if(command[1].length() > 0){
                    sendMessageFromList(message,GoogleSheetWorker.getMemberByID(command[1]));
                }
            }

            if(message.getText().contains("/myid")){
                sendMsg(message,"Your ID: " + message.getFrom().getId());
            }

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

    public String getBotUsername() {
        return BOTNAME;
    }
}
