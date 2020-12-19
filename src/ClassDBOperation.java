import java.sql.*;
import java.util.Vector;

public class ClassDBOperation {

    private static final String JDBC_TABLE = "students";
    private static final String JDBC_PORT = "3306";
    private static final String JDBC_DATABASE = "javadb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    private static final int GREATER_THAN = 1;
    private static final int NO_GREATER_THAN = 2;
    private static final int LESS_THAN = 3;
    private static final int NO_LESS_THAN = 4;
    private static final int EQUAL = 5;
    private static final int NOT_EQUAL = 6;

    public static void main(String[] args) throws Throwable {
        // 加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String JDBC_URL = "jdbc:mysql://localhost:" + JDBC_PORT + "/" + JDBC_DATABASE + "?useSSL=false&characterEncoding=utf8";

        // 建立连接
        TableManager tableManager = new TableManager(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
        tableManager.assignTable(JDBC_TABLE);
        // 显示全表
        tableManager.display();
        // 增
        tableManager.add(9,"Ranker",0,60);
        tableManager.display();
        // 删
        tableManager.startCondition("id",EQUAL,5).remove();
        tableManager.display();
        // 查
        tableManager.startCondition("score",GREATER_THAN,85).show();
        // 改
        tableManager.startCondition("name",EQUAL,"Rushmore").update("score",100);
        tableManager.display();

        tableManager.close();
    }
}
class TableManager{

    private static final int GREATER_THAN = 1;
    private static final int NO_GREATER_THAN = 2;
    private static final int LESS_THAN = 3;
    private static final int NO_LESS_THAN = 4;
    private static final int EQUAL = 5;
    private static final int NOT_EQUAL = 6;

    Connection connection;
    String tableName = "";
    Vector<TableRow> vector;
    boolean[] indexList;
    TableManager(String url,String user, String passwd) throws SQLException {
        connection = DriverManager.getConnection(url,user,passwd);
    }
    // 将表填入vector
    public void assignTable(String table) throws SQLException {
        if (table.isEmpty()){
            System.out.println("Table name cannot be empty!");
            return;
        }
        if (!tableName.isEmpty()){
            this.saveTable();
        }
        tableName = table;
        vector = new Vector<>();
        String selectAll = "select * from " + tableName +";";
        PreparedStatement preparedStatement = connection.prepareStatement(selectAll);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int id  = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int gender = resultSet.getInt(3);
            int score = resultSet.getInt(4);
            vector.add(new TableRow(id,name,gender,score));
        }
    }
    public void display(){
        System.out.println("id | name | gender | score");
        System.out.println("--------------------------");
        for (TableRow item : vector){
            System.out.println(item.id+"  "+item.name+"  "+item.gender+"  "+item.score);
        }
        System.out.println("\n");
    }
    public void add(int id, String name, int gender, int score){
        vector.add(new TableRow(id,name,gender,score));
    }
    public TableManager startCondition(String attr, int op, Object content){
        indexList = new boolean[vector.size()];
        for (int i = 0; i < vector.size(); i++){
            indexList[i] = true;
        }
        this.and(attr,op,content);
        return this;
    }
    public TableManager and(String attr, int op, Object content){
        for (int index = 0; index < vector.size(); index++){
            TableRow item = vector.get(index);
            switch (attr){
                case "id": {
                    int value = Integer.parseInt(String.valueOf(content));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.id <= value) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.id > value) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.id >= value) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.id < value) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.id != value) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.id == value) indexList[index] = false;
                            break;
                    }
                    break;
                }
                case "name": {
                    String value = String.valueOf(content);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.name.compareTo(value) <= 0) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.name.compareTo(value) > 0) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.name.compareTo(value) >= 0) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.name.compareTo(value) < 0) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.name.compareTo(value) != 0) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.name.compareTo(value) == 0) indexList[index] = false;
                            break;
                    }
                    break;
                }
                case "gender": {
                    int value = Integer.parseInt(String.valueOf(content));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.gender <= value) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.gender > value) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.gender >= value) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.gender < value) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.gender != value) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.gender == value) indexList[index] = false;
                            break;
                    }
                    break;
                }
                case "score": {
                    int value = Integer.parseInt(String.valueOf(content));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.score <= value) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.score > value) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.score >= value) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.score < value) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.score != value) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.score == value) indexList[index] = false;
                            break;
                    }
                    break;
                }
            }
        }
        return this;
    }
    public TableManager or(String attr, int op, Object content){
        for (int index = 0; index < vector.size(); index++){
            TableRow item = vector.get(index);
            switch (attr){
                case "id": {
                    int value = Integer.parseInt(String.valueOf(content));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.id > value) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.id <= value) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.id < value) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.id >= value) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.id == value) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.id != value) indexList[index] = true;
                            break;
                    }
                    break;
                }
                case "name": {
                    String value = String.valueOf(content);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.name.compareTo(value) > 0) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.name.compareTo(value) <= 0) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.name.compareTo(value) < 0) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.name.compareTo(value) >= 0) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.name.compareTo(value) == 0) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.name.compareTo(value) != 0) indexList[index] = true;
                            break;
                    }
                    break;
                }
                case "gender": {
                    int value = Integer.parseInt(String.valueOf(content));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.gender > value) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.gender <= value) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.gender < value) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.gender >= value) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.gender == value) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.gender != value) indexList[index] = true;
                            break;
                    }
                    break;
                }
                case "score": {
                    int value = Integer.parseInt(String.valueOf(content));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.score > value) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.score <= value) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.score < value) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.score >= value) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.score == value) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.score != value) indexList[index] = true;
                            break;
                    }
                    break;
                }
            }
        }
        return this;
    }
    public void show(){
        System.out.println("id | name | gender | score");
        System.out.println("--------------------------");
        for (int i = 0; i < vector.size(); i++) {
            if (indexList[i]){
                TableRow item = vector.get(i);
                System.out.println(item.id+"  "+item.name+"  "+item.gender+"  "+item.score);
            }
        }
        System.out.println("\n");
    }
    public int remove(){
        Vector<TableRow> temp = new Vector<>();
        for (int i = 0; i < vector.size(); i++) {
            if (!indexList[i]){
                TableRow item = vector.get(i);
                temp.add(item);
            }
        }
        vector = temp;
        return vector.size();
    }
    public int update(String attr, Object value){
        int count = 0;
        for (int i = 0; i < vector.size(); i++){
            if (indexList[i]){
                count++;
                switch (attr){
                    case "id":{
                        vector.get(i).id = Integer.parseInt(String.valueOf(value));
                        break;
                    }
                    case "name":{
                        vector.get(i).name = String.valueOf(value);
                        break;
                    }
                    case "gender":{
                        vector.get(i).gender = Integer.parseInt(String.valueOf(value));
                        break;
                    }
                    case "score":{
                        vector.get(i).score = Integer.parseInt(String.valueOf(value));
                        break;
                    }
                }
            }
        }
        return count;
    }
    public void close() throws SQLException {
        this.saveTable();
        connection.close();
    }

    //先清空表，然后使用vector重新填充表
    public void saveTable() throws SQLException {
        String truncate = "truncate " + tableName + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(truncate);
        preparedStatement.executeUpdate();
        String insert = "insert into " + tableName + " values (?,?,?,?);";
        preparedStatement = connection.prepareStatement(insert);
        for (TableRow item: vector){
            preparedStatement.setObject(1,item.id);
            preparedStatement.setObject(2,item.name);
            preparedStatement.setObject(3,item.gender);
            preparedStatement.setObject(4,item.score);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();
    }
}

class TableRow{
    int id;
    String name;
    int gender;
    int score;
    TableRow(int id, String name, int gender, int score){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.score = score;
    }
}