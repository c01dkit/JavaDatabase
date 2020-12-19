DROP DATABASE IF EXISTS javadb;
CREATE DATABASE javadb;
USE javadb;
CREATE TABLE students (
  id INT NOT NULL,
  name VARCHAR(20) NOT NULL,
  gender INT NOT NULL,
  score INT NOT NULL
);

INSERT INTO students  VALUES ( 1 ,  'Aamot' , 1 , 83 );
INSERT INTO students  VALUES ( 2 , 'Abadie' , 0 , 87 );
INSERT INTO students  VALUES ( 3 , 'Eric' , 1 , 81 );
INSERT INTO students  VALUES ( 4 , 'Heckenberg' , 0 , 82 );
INSERT INTO students  VALUES ( 5 , 'Hedemann' , 1 , 73 );
INSERT INTO students  VALUES ( 6 , 'Matsubara' , 1 , 65 );
INSERT INTO students  VALUES ( 7 , 'Rushmore' , 1 , 99 );
INSERT INTO students  VALUES ( 8 , 'Vecker' , 0 , 63 );

CREATE TABLE classes (
  id INT NOT NULL,
  points REAL NOT NULL,
  coursename VARCHAR(40) NOT NULL,
  teacher VARCHAR(20) NOT NULL,
  classroom VARCHAR(20) NOT NULL
);

INSERT INTO classes  VALUES ( 1 ,  4.5 , "ComputerNetwork" , "Lucy" , "Room202");
INSERT INTO classes  VALUES ( 2 ,  3 , "Java" , "MrLou" , "Room201");
INSERT INTO classes  VALUES ( 3 ,  5 , "OperationSystem" , "Jack" , "Room203");
INSERT INTO classes  VALUES ( 4 ,  4 , "InfomationVis" , "Rody" , "Room204");