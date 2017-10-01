package java;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Commands implements EventCommand {
    long chat_id;
    String first_name;
    String last_name;
    String username;

    public Commands(long chat_id, String first_name, String last_name, String username) {
        this.chat_id = chat_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
    }

    @Override
    public SendMessage start() throws SQLException, ClassNotFoundException {
        Database database = new Database(chat_id, first_name, last_name, username);

        String text = "Hi, I'm a bot-task. I was meant to measure your tasks.\n" +
                "I will help control your tasks.\n" +
                "To add a task, just write me it.\n" +
                "/start - instruction\n" +
                "/key - keyboard\n" +
                "/select - select a your task\n" +
                "/remove - del. task";
        SendMessage message = new SendMessage().setChatId(chat_id).setText(text);
        try {
            //Database database = new Database(chat_id, first_name, last_name, username);
            database.connect();
            database.createTableUser();
            database.writeUser();
        } catch (SQLException e) {
            e.printStackTrace();
            return message.setText(text+" \nYou are already in the database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;

    }

    @Override
    public SendMessage select(long chat_id) throws SQLException, ClassNotFoundException {
        Database database = new Database(chat_id, first_name, last_name, username);

        List title = database.selectionTasks(chat_id);
        if (title.equals(null)) {
            SendMessage message = new SendMessage().setChatId(chat_id).setText("No data");
            return message;
        } else {
            SendMessage message = new SendMessage().setChatId(chat_id).setText(String.valueOf(title));
            return message;

        }
    }
    @Override
    public SendMessage add(String message_text) throws SQLException, ClassNotFoundException {
        Database database = new Database(chat_id, first_name, last_name, username);

        database.connect();
            database.createTableTask();
            database.writeTask(message_text);

            SendMessage message = new SendMessage().setChatId(chat_id).setText(message_text +" added by your tasks");
            return message;
    }

    @Override
    public SendMessage keyboard() {
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText("Here is your keyboard");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("BEGIN");
        row.add("I finished");
        keyboard.add(row);

        row = new KeyboardRow();

        row.add("END");
        row.add("Select");

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    @Override
    public SendMessage begin(String title, long chat_id) {
        Stopwatch.setStatus(true);
        Stopwatch.Watch();
        SendMessage message = new SendMessage().setChatId(chat_id).setText("Go-go");
        return message;
    }

    @Override
    public SendMessage end() {
        return null;
    }


//    public SendMessage finish() throws SQLException, ClassNotFoundException {
//        Database database = new Database(chat_id, first_name, last_name, username);
//        SendMessage message = new SendMessage().setChatId(chat_id).setText(String.valueOf(database.writeMeta()));
//        return message;
////        database.endWork(chat_id, title);
//    }
}
