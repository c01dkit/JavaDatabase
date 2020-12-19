import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class ClassDBCreator {
    // final变量为参数配置部分，可以选择连接的用户名、密码、端口、数据库、数据表
    // 除了以下五处赋值外，本java源程序其他地方不需要修改
    // 编译运行本程序将生成指定表的管理java文件，需要在新生成的java中根据需要来编写操作的逻辑
    // 具体的操作逻辑详见报告说明
    private static final String JDBC_TABLE = "students";
    private static final String JDBC_PORT = "3306";
    private static final String JDBC_DATABASE = "javadb";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    private static Connection connection;
    private static String CLASS_NAME;
    private static String TABLE_NAME;
    private static String ROW_NAME;
    private static FileWriter fileWriter;
    private static String[][] mapper = null;
    public static void main(String[] args) throws IOException, SQLException {
        // 加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String JDBC_URL = "jdbc:mysql://localhost:" + JDBC_PORT + "/" + JDBC_DATABASE + "?useSSL=false&characterEncoding=utf8";
        connection = DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
        CLASS_NAME = "DB_"+JDBC_DATABASE+"_"+JDBC_TABLE;
        TABLE_NAME = "TABLE_"+JDBC_DATABASE+"_"+JDBC_TABLE;
        ROW_NAME = "ROW_"+JDBC_DATABASE+"_"+JDBC_TABLE;
        File file = new File(CLASS_NAME +".java");
        file.createNewFile();
        fileWriter = new FileWriter(file);
        init();
        genMain();
        genTableStart();
        genAssignTable();
        genDisplay();
        genAdd();
        genStartCondition();
        genAnd();
        genOr();
        genShow();
        genRemove();
        genUpdate();
        genSaveTable();
        genTableEnd();
        genRow();
        fileWriter.close();
        connection.close();
        System.out.println("文件生成并存放于 "+file.getAbsolutePath());
    }
    private static void init() throws SQLException {
        String sql = "show create table ";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql+JDBC_TABLE)){
            resultSet.next();
            String tableInfo = resultSet.getString(2);
            String[] attr = tableInfo.split("\n");
            if (attr.length - 2 <= 0) return;
            mapper = new String[attr.length-2][2];
            for (int i = 1; i < attr.length-1; i++){
                String[] temp = attr[i].split(" ");
                mapper[i-1][0] = temp[2].replace("`","");
                if (temp[3].contains("bit") || temp[3].contains("bool")) mapper[i-1][1] = "boolean";
                else if (temp[3].contains("bigint")) mapper[i-1][1] = "long";
                else if (temp[3].contains("int")) mapper[i-1][1] = "int";
                else if (temp[3].contains("char")) mapper[i-1][1] = "String";
                else if (temp[3].contains("real")) mapper[i-1][1] = "float";
                else if (temp[3].contains("float") || temp[3].contains("double")) mapper[i-1][1] = "double";
                else if (temp[3].contains("decimal")) mapper[i-1][1] = "BigDecimal";
            }
        }
    }
    private static void genMain() throws IOException {
        fileWriter.write("import java.sql.*;\nimport java.util.Vector;\n\npublic class " + CLASS_NAME +
                " {\n\n    private static final String JDBC_TABLE = \"" + JDBC_TABLE +
                "\";\n    private static final String JDBC_PORT = \"" + JDBC_PORT +
                "\";\n    private static final String JDBC_DATABASE = \"" + JDBC_DATABASE +
                "\";\n    private static final String JDBC_USER = \"" + JDBC_USER +
                "\";\n    private static final String JDBC_PASSWORD = \"" + JDBC_PASSWORD +
                "\";\n" +
                "\n" +
                "    private static final int GREATER_THAN = 1;\n" +
                "    private static final int NO_GREATER_THAN = 2;\n" +
                "    private static final int LESS_THAN = 3;\n" +
                "    private static final int NO_LESS_THAN = 4;\n" +
                "    private static final int EQUAL = 5;\n" +
                "    private static final int NOT_EQUAL = 6;\n\n");
        fileWriter.write("    public static void main(String[] args) throws Throwable {\n" +
                "        // 加载驱动\n" +
                "        try {\n" +
                "            Class.forName(\"com.mysql.jdbc.Driver\");\n" +
                "        } catch (ClassNotFoundException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        String JDBC_URL = \"jdbc:mysql://localhost:\" + JDBC_PORT + \"/\" + JDBC_DATABASE + \"?useSSL=false&characterEncoding=utf8\";\n" +
                "\n" +
                "        // 建立连接\n" +
                "        "+TABLE_NAME+" table = new "+TABLE_NAME+"(JDBC_URL,JDBC_USER,JDBC_PASSWORD);\n" +
                "        table.assignTable(JDBC_TABLE);\n" +
                "        // 显示全表\n" +
                "        table.display();\n" +
                "        // 在这里根据业务需要填写操作，用法参见报告\n\n\n" +
                "        table.close();\n" +
                "    }\n" +
                "}\n");
        fileWriter.flush();
    }
    private static void genRow() throws IOException {
        fileWriter.write("class "+ROW_NAME+"{\n");
        for (String[] strings : mapper) {
            fileWriter.write("    " + strings[1] + " " + strings[0] + ";\n");
        }
        fileWriter.write("    "+ROW_NAME+"("+mapper[0][1]+" "+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++){
            fileWriter.write(", "+mapper[i][1]+" "+mapper[i][0]);
        }
        fileWriter.write("){\n");
        for (String[] strings: mapper){
            fileWriter.write("        this."+strings[0]+" = "+strings[0]+";\n");
        }
        fileWriter.write("    }\n}\n");
        fileWriter.flush();
    }
    private static void genTableStart() throws IOException {
        fileWriter.write("class "+TABLE_NAME+"{\n" +
                "\n" +
                "    private static final int GREATER_THAN = 1;\n" +
                "    private static final int NO_GREATER_THAN = 2;\n" +
                "    private static final int LESS_THAN = 3;\n" +
                "    private static final int NO_LESS_THAN = 4;\n" +
                "    private static final int EQUAL = 5;\n" +
                "    private static final int NOT_EQUAL = 6;\n" +
                "\n" +
                "    Connection connection;\n" +
                "    String tableName = \"\";\n" +
                "    Vector<"+ROW_NAME+"> vector;\n" +
                "    boolean[] indexList;\n" +
                "    "+TABLE_NAME+"(String url,String user, String passwd) throws SQLException {\n" +
                "        connection = DriverManager.getConnection(url,user,passwd);\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genAssignTable() throws IOException{
        fileWriter.write("    // 将表填入vector\n" +
                "    public void assignTable(String table) throws SQLException {\n" +
                "        if (table.isEmpty()){\n" +
                "            System.out.println(\"Table name cannot be empty!\");\n" +
                "            return;\n" +
                "        }\n" +
                "        if (!tableName.isEmpty()){\n" +
                "            this.saveTable();\n" +
                "        }\n" +
                "        tableName = table;\n" +
                "        vector = new Vector<>();\n" +
                "        String selectAll = \"select * from \" + tableName +\";\";\n" +
                "        PreparedStatement preparedStatement = connection.prepareStatement(selectAll);\n" +
                "        ResultSet resultSet = preparedStatement.executeQuery();\n" +
                "        while (resultSet.next()){\n");
        for (int i = 0; i < mapper.length; i++){
            fileWriter.write("            "+defineVarsInResultSet(mapper[i][1],mapper[i][0],i+1));
        }
        fileWriter.write("            vector.add(new "+ROW_NAME+"("+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++){
            fileWriter.write(", "+mapper[i][0]);
        }
        fileWriter.write("));\n        }\n    }\n");
        fileWriter.flush();
    }
    private static void genDisplay() throws IOException{
        fileWriter.write("    public void display(){\n");
        StringBuilder temp = new StringBuilder();
        int width = mapper[0][0].length();
        fileWriter.write("        System.out.println(\""+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++) {
            fileWriter.write(" | " + mapper[i][0]);
            width += mapper[i][0].length()+3;
        }
        fileWriter.write("\");\n        System.out.println(\"");
        for (int i = 0; i < width; i++){
            temp.append("-");
        }
        fileWriter.write(temp.toString()+"\");\n        for ("+ROW_NAME+" item : vector){\n" +
                "            System.out.println(");
        fileWriter.write("item."+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++) {
            fileWriter.write("+\"  \"+item." + mapper[i][0]);
        }
        fileWriter.write(");\n" +
                "        }\n" +
                "        System.out.println(\"\\n\");\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genAdd() throws IOException{
        fileWriter.write("        public void add("+mapper[0][1]+" "+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++) {
            fileWriter.write(", "+mapper[i][1]+" "+mapper[i][0]);
        }
        fileWriter.write("){\n        vector.add(new "+ROW_NAME+"("+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++) {
            fileWriter.write(", "+mapper[i][0]);
        }
        fileWriter.write("));\n}\n");
        fileWriter.flush();
    }
    private static void genStartCondition() throws IOException{
        fileWriter.write("        public "+TABLE_NAME+" startCondition(String attr, int op, Object content){\n" +
                "            indexList = new boolean[vector.size()];\n" +
                "            for (int i = 0; i < vector.size(); i++){\n" +
                "                indexList[i] = true;\n" +
                "            }\n" +
                "            this.and(attr,op,content);\n" +
                "            return this;\n" +
                "        }\n");
        fileWriter.flush();
    }
    private static void genAnd() throws IOException{
        fileWriter.write("    public "+TABLE_NAME+" and(String attr, int op, Object value){\n" +
                "        for (int index = 0; index < vector.size(); index++){\n" +
                "            "+ROW_NAME+" item = vector.get(index);\n" +
                "            switch (attr){\n");
        for (String[] strings:mapper){
            fileWriter.write(defineVarsInAnd(strings[0],strings[1]));
        }
        fileWriter.write("            }\n" +
                "        }\n" +
                "        return this;\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genOr() throws IOException{
        fileWriter.write("    public "+TABLE_NAME+" or(String attr, int op, Object value){\n" +
                "        for (int index = 0; index < vector.size(); index++){\n" +
                "            "+ROW_NAME+" item = vector.get(index);\n" +
                "            switch (attr){\n");
        for (String[] strings:mapper){
            fileWriter.write(defineVarsInOr(strings[0],strings[1]));
        }
        fileWriter.write("            }\n" +
                "        }\n" +
                "        return this;\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genShow() throws IOException{
        fileWriter.write("    public void show(){\n");
        StringBuilder temp = new StringBuilder();
        int width = mapper[0][0].length();
        fileWriter.write("        System.out.println(\""+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++) {
            fileWriter.write(" | " + mapper[i][0]);
            width += mapper[i][0].length()+3;
        }
        fileWriter.write("\");\n        System.out.println(\"");
        for (int i = 0; i < width; i++){
            temp.append("-");
        }
        fileWriter.write(temp.toString()+"\");\n        for (int i = 0; i < vector.size(); i++) {\n" +
                "            if (indexList[i]){\n" +
                "                "+ROW_NAME+" item = vector.get(i);\n" +
                "                System.out.println(");
        fileWriter.write("item."+mapper[0][0]);
        for (int i = 1; i < mapper.length; i++) {
            fileWriter.write("+\"  \"+item." + mapper[i][0]);
        }
        fileWriter.write(");\n" +
                "            }\n" +
                "        }\n" +
                "        System.out.println(\"\\n\");\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genRemove() throws IOException{
        fileWriter.write("    public int remove(){\n" +
                "        Vector<"+ROW_NAME+"> temp = new Vector<>();\n" +
                "        for (int i = 0; i < vector.size(); i++) {\n" +
                "            if (!indexList[i]){\n" +
                "                "+ROW_NAME+" item = vector.get(i);\n" +
                "                temp.add(item);\n" +
                "            }\n" +
                "        }\n" +
                "        vector = temp;\n" +
                "        return vector.size();\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genUpdate() throws IOException{
        fileWriter.write("    public int update(String attr, Object value){\n" +
                "        int count = 0;\n" +
                "        for (int i = 0; i < vector.size(); i++){\n" +
                "            if (indexList[i]){\n" +
                "                count++;\n" +
                "                switch (attr){\n");
        for (String[] strings:mapper){
            fileWriter.write("                    case \""+strings[0]+"\":{\n"+
                   "                        vector.get(i)." + strings[0] + " = ");
            fileWriter.write(defineVarsInCase(strings[1]));
            fileWriter.write("                        break;\n" +
                    "                    }\n");
        }
        fileWriter.write("                }\n" +
                "            }\n" +
                "        }\n" +
                "        return count;\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genSaveTable() throws IOException{
        fileWriter.write("    //先清空表，然后使用vector重新填充表\n" +
                "    public void saveTable() throws SQLException {\n" +
                "        String truncate = \"truncate \" + tableName + \";\";\n" +
                "        PreparedStatement preparedStatement = connection.prepareStatement(truncate);\n" +
                "        preparedStatement.executeUpdate();\n" +
                "        String insert = \"insert into \" + tableName + \" values (?");
        for (int i = 1; i < mapper.length; i++){
            fileWriter.write(",?");
        }
        fileWriter.write(");\";\n" +
                "        preparedStatement = connection.prepareStatement(insert);\n" +
                "        for ("+ROW_NAME+" item: vector){\n");
        for (int i = 0; i < mapper.length; i++) {
            fileWriter.write("            preparedStatement.setObject("+(i+1)+",item."+mapper[i][0]+");\n");
        }
        fileWriter.write("            preparedStatement.executeUpdate();\n" +
                "        }\n" +
                "        preparedStatement.close();\n" +
                "    }\n");
        fileWriter.flush();
    }
    private static void genTableEnd() throws IOException{
        fileWriter.write("    public void close() throws SQLException {\n" +
                "        this.saveTable();\n" +
                "        connection.close();\n" +
                "    }\n}\n");
        fileWriter.flush();
    }
    private static String defineVarsInResultSet(String type, String key, int number){
        String temp = "";
        switch (type) {
            case "boolean":
                temp = "boolean " + key + " = resultSet.getBoolean(" + number + ");\n";
                break;
            case "long":
                temp = "long " + key + " = resultSet.getLong(" + number + ");\n";
                break;
            case "int":
                temp = "int " + key + " = resultSet.getInt(" + number + ");\n";
                break;
            case "String":
                temp = "String " + key + " = resultSet.getString(" + number + ");\n";
                break;
            case "float":
                temp = "float " + key + " = resultSet.getFloat(" + number + ");\n";
                break;
            case "double":
                temp = "double " + key + " = resultSet.getDouble(" + number + ");\n";
                break;
            case "BigDecimal":
                temp = "BigDecimal " + key + " = resultSet.getBigDecimal(" + number + ");\n";
                break;
        }
        return temp;
    }
    private static String defineVarsInCase(String type){
        String temp = "";
        switch (type){
            case "int":{
                temp = "Integer.parseInt(String.valueOf(value));\n";
                break;
            }
            case "float":{
                temp = "Float.parseFloat(String.valueOf(value));\n";
                break;
            }
            case "boolean":{
                temp = "Boolean.parseBoolean(String.valueOf(value));\n";
                break;
            }
            case "long":{
                temp = "Long.parseLong(String.valueOf(value));\n";
                break;
            }
            case "BigDecimal":{
                temp = "new BigDecimal(String.valueOf(value));\n";
                break;
            }
            case "String":{
                temp = "String.valueOf(value);\n";
                break;
            }
            case "double":{
                temp = "Double.parseDouble(String.valueOf(value));\n";
                break;
            }
        }
        return temp;
    }
    private static String defineVarsInAnd(String key, String type) {
        if (type.equals("String"))
        return "        case \"" + key + "\": {\n            " + type +
                " val = " + defineVarsInCase(type) +
                "            switch (op) {\n" +
                "                case GREATER_THAN:\n" +
                "                    if (item." +
                key +
                ".compareTo(val) <= 0) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case NO_GREATER_THAN:\n" +
                "                    if (item." +
                key +
                ".compareTo(val) > 0) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case LESS_THAN:\n" +
                "                    if (item." +
                key +
                ".compareTo(val) >= 0) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case NO_LESS_THAN:\n" +
                "                    if (item." +
                key +
                ".compareTo(val) < 0) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case EQUAL:\n" +
                "                    if (item." +
                key +
                ".compareTo(val) != 0) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case NOT_EQUAL:\n" +
                "                    if (item." +
                key +
                ".compareTo(val) == 0) indexList[index] = false;\n" +
                "                    break;\n" +
                "            }\n" +
                "            break;\n" +
                "        }\n";
        else return "        case \"" + key + "\": {\n            " + type +
                " val = " + defineVarsInCase(type) +
                "            switch (op) {\n" +
                "                case GREATER_THAN:\n" +
                "                    if (item." +
                key +
                " <= val) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case NO_GREATER_THAN:\n" +
                "                    if (item." +
                key +
                " > val) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case LESS_THAN:\n" +
                "                    if (item." +
                key +
                " >= val) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case NO_LESS_THAN:\n" +
                "                    if (item." +
                key +
                " < val) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case EQUAL:\n" +
                "                    if (item." +
                key +
                " != val) indexList[index] = false;\n" +
                "                    break;\n" +
                "                case NOT_EQUAL:\n" +
                "                    if (item." +
                key +
                " == val) indexList[index] = false;\n" +
                "                    break;\n" +
                "            }\n" +
                "            break;\n" +
                "        }\n";
    }
    private static String defineVarsInOr(String key, String type) {
        if (type.equals("String"))
            return "        case \"" + key + "\": {\n            " + type +
                    " val = " + defineVarsInCase(type) +
                    "            switch (op) {\n" +
                    "                case GREATER_THAN:\n" +
                    "                    if (item." +
                    key +
                    ".compareTo(val) > 0) indexList[index] = true;\n" +
                    "                    break;\n" +
                    "                case NO_GREATER_THAN:\n" +
                    "                    if (item." +
                    key +
                    ".compareTo(val) <= 0) indexList[index] = true;\n" +
                    "                    break;\n" +
                    "                case LESS_THAN:\n" +
                    "                    if (item." +
                    key +
                    ".compareTo(val) < 0) indexList[index] = true;\n" +
                    "                    break;\n" +
                    "                case NO_LESS_THAN:\n" +
                    "                    if (item." +
                    key +
                    ".compareTo(val) >= 0) indexList[index] = true;\n" +
                    "                    break;\n" +
                    "                case EQUAL:\n" +
                    "                    if (item." +
                    key +
                    ".compareTo(val) == 0) indexList[index] = true;\n" +
                    "                    break;\n" +
                    "                case NOT_EQUAL:\n" +
                    "                    if (item." +
                    key +
                    ".compareTo(val) != 0) indexList[index] = true;\n" +
                    "                    break;\n" +
                    "            }\n" +
                    "            break;\n" +
                    "        }\n";
        else return "        case \"" + key + "\": {\n            " + type +
                " val = " + defineVarsInCase(type) +
                "            switch (op) {\n" +
                "                case GREATER_THAN:\n" +
                "                    if (item." +
                key +
                " > val) indexList[index] = true;\n" +
                "                    break;\n" +
                "                case NO_GREATER_THAN:\n" +
                "                    if (item." +
                key +
                " <= val) indexList[index] = true;\n" +
                "                    break;\n" +
                "                case LESS_THAN:\n" +
                "                    if (item." +
                key +
                " < val) indexList[index] = true;\n" +
                "                    break;\n" +
                "                case NO_LESS_THAN:\n" +
                "                    if (item." +
                key +
                " >= val) indexList[index] = true;\n" +
                "                    break;\n" +
                "                case EQUAL:\n" +
                "                    if (item." +
                key +
                " == val) indexList[index] = true;\n" +
                "                    break;\n" +
                "                case NOT_EQUAL:\n" +
                "                    if (item." +
                key +
                " != val) indexList[index] = true;\n" +
                "                    break;\n" +
                "            }\n" +
                "            break;\n" +
                "        }\n";
    }
}
