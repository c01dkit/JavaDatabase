import java.sql.*;

public class BasicDBOperation {
    public static void main(String[] args) throws SQLException {
        // 加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // JDBC连接的URL
        String JDBC_URL = "jdbc:mysql://localhost:3306/javadb?useSSL=false&characterEncoding=utf8";
        String JDBC_USER = "root";
        String JDBC_PASSWORD = "";
        // 获取连接
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        Statement statement = connection.createStatement()){
            //查
            try(ResultSet resultSet = statement.executeQuery("select * from students;")){
                System.out.println("查询：select * from students;");
                System.out.println("id | name | gender | score");
                System.out.println("--------------------------");
                while (resultSet.next()){
                    System.out.println(resultSet.getLong("id")+" | "
                    +resultSet.getString("name") + " | "
                    +resultSet.getInt("gender") + " | "
                    +resultSet.getInt("score"));
                }
            }
            //增
            statement.executeUpdate("insert into students values (9,'JavaTest', 0, 100);");
            try(ResultSet resultSet = statement.executeQuery("select * from students;")){
                System.out.println("\n插入：insert into students (name, gender, score) values ('JavaTest', 0, 100);");
                System.out.println("id | name | gender | score");
                System.out.println("--------------------------");
                while (resultSet.next()){
                    System.out.println(resultSet.getLong("id")+" | "
                            +resultSet.getString("name") + " | "
                            +resultSet.getInt("gender") + " | "
                            +resultSet.getInt("score"));
                }
            }
            //改
            statement.executeUpdate("update javadb.students set name='newName',score=95 where name='JavaTest';");
            try(ResultSet resultSet = statement.executeQuery("select * from students;")){
                System.out.println("\n修改：update javadb.students set name='newName',score=95 where name='JavaTest';");
                System.out.println("id | name | gender | score");
                System.out.println("--------------------------");
                while (resultSet.next()){
                    System.out.println(resultSet.getLong("id")+" | "
                            +resultSet.getString("name") + " | "
                            +resultSet.getInt("gender") + " | "
                            +resultSet.getInt("score"));
                }
            }
            //删
            statement.executeUpdate("delete from javadb.students where name='newName';");
            try(ResultSet resultSet = statement.executeQuery("select * from students;")){
                System.out.println("\n删除：delete from javadb.students where name='newName';");
                System.out.println("id | name | gender | score");
                System.out.println("--------------------------");
                while (resultSet.next()){
                    System.out.println(resultSet.getLong("id")+" | "
                            +resultSet.getString("name") + " | "
                            +resultSet.getInt("gender") + " | "
                            +resultSet.getInt("score"));
                }
            }

        }

    }
}
