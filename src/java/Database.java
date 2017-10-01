package java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    long chat_id;
    String first_name;
    String last_name;
    String username;

    public Database() {
    }

    public Database(long chat_id, String first_name, String last_name, String username) {
        this.chat_id = chat_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;

    }

    public static void connect() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:task.db");
        System.out.println("Opened database successfully");
    }

    //Create table
//------------------------------------------------------
    public void createTableUser() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY," +
                " 'fist_name' text," +
                " 'last_name' text," +
                "'username' text);");
        System.out.println("Table created or already exists.");
    }


    public void createTableTask() throws ClassNotFoundException, SQLException {
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'task' ('chat_id' INTEGER," +
                " 'title_task' text," +
                " 'time_begin' text," +
                "'time_end' text," +
                "'time_work' INTEGER);");
        System.out.println("Table created or already exists.");
    }
    public void createTableMeta()throws ClassNotFoundException, SQLException{
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'meta' ('chat_id' INTEGER," +
                " 'quantity' INTEGER;");
    }
    //------------------------------------------------------
    public void writeUser() throws SQLException{
        statmt.execute("INSERT INTO 'users' ('id', 'fist_name', 'last_name', 'username')" +
                " VALUES ("+chat_id+",'"+first_name+"','"+last_name+"','"+username+"'); ");
        System.out.println("user added");
    }

    public void writeTask(String title_task) throws SQLException{
        statmt.execute("INSERT INTO 'task' (chat_id,title_task) VALUES ("+chat_id+", '"+title_task+"')");
        beginWork(chat_id, title_task);
        System.out.println("task added");
    }
    public void writeMeta(long chat_id) throws SQLException{
        int i = selectionTasks(chat_id).size();
        statmt.execute("INSERT INTO 'meta' (chat_id,quantity) VALUE ("+chat_id+","+i+")");
    }

    public void beginWork(long chat_id, String title_task) throws SQLException {
        statmt.execute("UPDATE 'task' SET 'time_begin' = datetime('now')," +
                "'time_work' = '0'" +
                "WHERE chat_id = "+chat_id+" AND title_task = '"+title_task+"';");
        System.out.println("beginwork");
    }

    public void endWork(long chat_id, String title_task) throws SQLException {
        statmt.execute("UPDATE 'task' SET 'time_end' = datetime('now')\n" +
                "WHERE chat_id = "+chat_id+" AND title_task = '"+title_task+"';");
        System.out.println("endwork");
    }

    public void setTimeWork(long seconds, String title)throws SQLException {
        statmt.execute("UPDATE 'task' SET 'time_work' = "+seconds+"\n" +
                "WHERE chat_id = "+chat_id+" AND title_task = "+title+";");
    }
    public String getTimeWork(long chat_id, String title_task) throws SQLException {
        String s = null;
        resSet = statmt.executeQuery("SELECT chat_id,title_task, time_work FROM task");
        while (resSet.next()){
            long id = resSet.getLong("chat_id");
            String titleFrom = resSet.getString("title_task");
            String timeFrom = resSet.getString("time_work");
            if (id == chat_id && title_task == titleFrom){
                s = timeFrom;
            }
        }
        return s;
    }

    public List selectionTasks(long chat_id) throws SQLException {
        resSet = statmt.executeQuery("SELECT chat_id,title_task FROM task");
        List title = new ArrayList();
        int i = 0;
        while (resSet.next()){
            long id = resSet.getLong("chat_id");
            String titleFrom = resSet.getString("title_task");
            if (id == chat_id){
                i++;
                title.add("/"+i+" "+titleFrom+"\n");
            }
        }
        return title;
    }
    public int getNumberOfTask(long chat_id) throws SQLException {
        resSet = statmt.executeQuery("SELECT chat_id FROM task");
        int i = 0;
        while (resSet.next()){
            long id = resSet.getLong("chat_id");
            if (id == chat_id){
                i++;
            }
        }
        return i;
    }
}