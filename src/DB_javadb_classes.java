import java.sql.*;
import java.util.Vector;

public class DB_javadb_classes {

    private static final String JDBC_TABLE = "classes";
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
        TABLE_javadb_classes table = new TABLE_javadb_classes(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
        table.assignTable(JDBC_TABLE);
        // 显示全表
        table.display();
        // 在这里根据业务需要填写操作，用法参见报告


        table.close();
    }
}
class TABLE_javadb_classes{

    private static final int GREATER_THAN = 1;
    private static final int NO_GREATER_THAN = 2;
    private static final int LESS_THAN = 3;
    private static final int NO_LESS_THAN = 4;
    private static final int EQUAL = 5;
    private static final int NOT_EQUAL = 6;

    Connection connection;
    String tableName = "";
    Vector<ROW_javadb_classes> vector;
    boolean[] indexList;
    TABLE_javadb_classes(String url,String user, String passwd) throws SQLException {
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
            int id = resultSet.getInt(1);
            double points = resultSet.getDouble(2);
            String coursename = resultSet.getString(3);
            String teacher = resultSet.getString(4);
            String classroom = resultSet.getString(5);
            vector.add(new ROW_javadb_classes(id, points, coursename, teacher, classroom));
        }
    }
    public void display(){
        System.out.println("id | points | coursename | teacher | classroom");
        System.out.println("----------------------------------------------");
        for (ROW_javadb_classes item : vector){
            System.out.println(item.id+"  "+item.points+"  "+item.coursename+"  "+item.teacher+"  "+item.classroom);
        }
        System.out.println("\n");
    }
    public void add(int id, double points, String coursename, String teacher, String classroom){
        vector.add(new ROW_javadb_classes(id, points, coursename, teacher, classroom));
    }
    public TABLE_javadb_classes startCondition(String attr, int op, Object content){
        indexList = new boolean[vector.size()];
        for (int i = 0; i < vector.size(); i++){
            indexList[i] = true;
        }
        this.and(attr,op,content);
        return this;
    }
    public TABLE_javadb_classes and(String attr, int op, Object value){
        for (int index = 0; index < vector.size(); index++){
            ROW_javadb_classes item = vector.get(index);
            switch (attr){
                case "id": {
                    int val = Integer.parseInt(String.valueOf(value));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.id <= val) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.id > val) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.id >= val) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.id < val) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.id != val) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.id == val) indexList[index] = false;
                            break;
                    }
                    break;
                }
                case "points": {
                    double val = Double.parseDouble(String.valueOf(value));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.points <= val) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.points > val) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.points >= val) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.points < val) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.points != val) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.points == val) indexList[index] = false;
                            break;
                    }
                    break;
                }
                case "coursename": {
                    String val = String.valueOf(value);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.coursename.compareTo(val) <= 0) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.coursename.compareTo(val) > 0) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.coursename.compareTo(val) >= 0) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.coursename.compareTo(val) < 0) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.coursename.compareTo(val) != 0) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.coursename.compareTo(val) == 0) indexList[index] = false;
                            break;
                    }
                    break;
                }
                case "teacher": {
                    String val = String.valueOf(value);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.teacher.compareTo(val) <= 0) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.teacher.compareTo(val) > 0) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.teacher.compareTo(val) >= 0) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.teacher.compareTo(val) < 0) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.teacher.compareTo(val) != 0) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.teacher.compareTo(val) == 0) indexList[index] = false;
                            break;
                    }
                    break;
                }
                case "classroom": {
                    String val = String.valueOf(value);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.classroom.compareTo(val) <= 0) indexList[index] = false;
                            break;
                        case NO_GREATER_THAN:
                            if (item.classroom.compareTo(val) > 0) indexList[index] = false;
                            break;
                        case LESS_THAN:
                            if (item.classroom.compareTo(val) >= 0) indexList[index] = false;
                            break;
                        case NO_LESS_THAN:
                            if (item.classroom.compareTo(val) < 0) indexList[index] = false;
                            break;
                        case EQUAL:
                            if (item.classroom.compareTo(val) != 0) indexList[index] = false;
                            break;
                        case NOT_EQUAL:
                            if (item.classroom.compareTo(val) == 0) indexList[index] = false;
                            break;
                    }
                    break;
                }
            }
        }
        return this;
    }
    public TABLE_javadb_classes or(String attr, int op, Object value){
        for (int index = 0; index < vector.size(); index++){
            ROW_javadb_classes item = vector.get(index);
            switch (attr){
                case "id": {
                    int val = Integer.parseInt(String.valueOf(value));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.id > val) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.id <= val) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.id < val) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.id >= val) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.id == val) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.id != val) indexList[index] = true;
                            break;
                    }
                    break;
                }
                case "points": {
                    double val = Double.parseDouble(String.valueOf(value));
                    switch (op) {
                        case GREATER_THAN:
                            if (item.points > val) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.points <= val) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.points < val) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.points >= val) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.points == val) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.points != val) indexList[index] = true;
                            break;
                    }
                    break;
                }
                case "coursename": {
                    String val = String.valueOf(value);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.coursename.compareTo(val) > 0) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.coursename.compareTo(val) <= 0) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.coursename.compareTo(val) < 0) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.coursename.compareTo(val) >= 0) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.coursename.compareTo(val) == 0) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.coursename.compareTo(val) != 0) indexList[index] = true;
                            break;
                    }
                    break;
                }
                case "teacher": {
                    String val = String.valueOf(value);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.teacher.compareTo(val) > 0) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.teacher.compareTo(val) <= 0) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.teacher.compareTo(val) < 0) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.teacher.compareTo(val) >= 0) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.teacher.compareTo(val) == 0) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.teacher.compareTo(val) != 0) indexList[index] = true;
                            break;
                    }
                    break;
                }
                case "classroom": {
                    String val = String.valueOf(value);
                    switch (op) {
                        case GREATER_THAN:
                            if (item.classroom.compareTo(val) > 0) indexList[index] = true;
                            break;
                        case NO_GREATER_THAN:
                            if (item.classroom.compareTo(val) <= 0) indexList[index] = true;
                            break;
                        case LESS_THAN:
                            if (item.classroom.compareTo(val) < 0) indexList[index] = true;
                            break;
                        case NO_LESS_THAN:
                            if (item.classroom.compareTo(val) >= 0) indexList[index] = true;
                            break;
                        case EQUAL:
                            if (item.classroom.compareTo(val) == 0) indexList[index] = true;
                            break;
                        case NOT_EQUAL:
                            if (item.classroom.compareTo(val) != 0) indexList[index] = true;
                            break;
                    }
                    break;
                }
            }
        }
        return this;
    }
    public void show(){
        System.out.println("id | points | coursename | teacher | classroom");
        System.out.println("----------------------------------------------");
        for (int i = 0; i < vector.size(); i++) {
            if (indexList[i]){
                ROW_javadb_classes item = vector.get(i);
                System.out.println(item.id+"  "+item.points+"  "+item.coursename+"  "+item.teacher+"  "+item.classroom);
            }
        }
        System.out.println("\n");
    }
    public int remove(){
        Vector<ROW_javadb_classes> temp = new Vector<>();
        for (int i = 0; i < vector.size(); i++) {
            if (!indexList[i]){
                ROW_javadb_classes item = vector.get(i);
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
                    case "points":{
                        vector.get(i).points = Double.parseDouble(String.valueOf(value));
                        break;
                    }
                    case "coursename":{
                        vector.get(i).coursename = String.valueOf(value);
                        break;
                    }
                    case "teacher":{
                        vector.get(i).teacher = String.valueOf(value);
                        break;
                    }
                    case "classroom":{
                        vector.get(i).classroom = String.valueOf(value);
                        break;
                    }
                }
            }
        }
        return count;
    }
    //先清空表，然后使用vector重新填充表
    public void saveTable() throws SQLException {
        String truncate = "truncate " + tableName + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(truncate);
        preparedStatement.executeUpdate();
        String insert = "insert into " + tableName + " values (?,?,?,?,?);";
        preparedStatement = connection.prepareStatement(insert);
        for (ROW_javadb_classes item: vector){
            preparedStatement.setObject(1,item.id);
            preparedStatement.setObject(2,item.points);
            preparedStatement.setObject(3,item.coursename);
            preparedStatement.setObject(4,item.teacher);
            preparedStatement.setObject(5,item.classroom);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();
    }
    public void close() throws SQLException {
        this.saveTable();
        connection.close();
    }
}
class ROW_javadb_classes{
    int id;
    double points;
    String coursename;
    String teacher;
    String classroom;
    ROW_javadb_classes(int id, double points, String coursename, String teacher, String classroom){
        this.id = id;
        this.points = points;
        this.coursename = coursename;
        this.teacher = teacher;
        this.classroom = classroom;
    }
}
