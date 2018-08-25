import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * Created by Admin on 18.08.2018.
 */
public class Main {

    public static void main(String[] args) {

            ApiContextInitializer.init();
            TelegramBotsApi api = new TelegramBotsApi();
            try {
                api.registerBot(new MainBotClass());
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
    }

}
