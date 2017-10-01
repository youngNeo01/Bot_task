package java;

import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.sql.SQLException;

public interface EventCommand {
    SendMessage start() throws SQLException, ClassNotFoundException;
    SendMessage select(long chat_id) throws SQLException, ClassNotFoundException;
    SendMessage add(String message_text) throws SQLException, ClassNotFoundException;
    SendMessage keyboard();
    SendMessage begin(String title, long chat_id);
    SendMessage end();
    //void finish(String title, long chat_id) throws SQLException, ClassNotFoundException;;
}
