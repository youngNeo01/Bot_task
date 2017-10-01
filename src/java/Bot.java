package java;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

public class Bot extends TelegramLongPollingBot{
    @Override
    public void onUpdateReceived(Update update) {
        long chat_id = update.getMessage().getChatId();
        String first_name = update.getMessage().getChat().getFirstName();
        String last_name = update.getMessage().getChat().getLastName();
        String username = update.getMessage().getChat().getUserName();
        Commands commands = new Commands(chat_id, first_name, last_name, username);
        if (update.hasMessage() && update.getMessage().hasText()){
            commands.keyboard();
            String message_text = update.getMessage().getText();
            boolean ex = true;

                ex = excptionName(message_text, chat_id);


            try {
                if(message_text.equals("/start")){ //start
                    sendMessage(commands.start());
                }
                if(message_text.equals("/key")){
                    sendMessage(commands.keyboard());
                }
                if(message_text.equals("/select")){
                    sendMessage(commands.select(chat_id));
                }
                if(message_text.equals("/begin") || message_text.equals("BEGIN")){

                }
                if(message_text.equals("/end") || message_text.equals("END")){
                    Stopwatch.setStatus(false);
                }

                if(message_text.equals("/finish") || message_text.equals("I finished")){
                    //sendMessage(commands.finish());
                }
                if (!message_text.equals(null) && ex == false){
                    sendMessage(commands.add(message_text));//add to database task
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean excptionName(String message_text, long chat_id) {
        Vector ex = new Vector();
        Enumeration enumr;

        ex.add("/start");
        ex.add("/key");
        ex.add("/select");
        ex.add("/finish");
        ex.add("Select");
        ex.add("/begin");
        ex.add("/end");
        ex.add("BEGIN");
        ex.add("END");
        ex.add("I finished");

        enumr = ex.elements();
        while (enumr.hasMoreElements()){
            if(message_text.equals(enumr.nextElement().toString())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getBotUsername() {
        return "TBot";
    }

    @Override
    public String getBotToken() {
        return "386311322:AAFdCVts8dQp6hh9R5yXytYfG8YnXRABk6c";
    }
}
