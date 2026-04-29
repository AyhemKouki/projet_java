package controller;

import database.DBConnection;
import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookController {

    public static void ListBooks() throws SQLException{
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        int count = 0;

        try{
            conn = DBConnection.connect();
            st = conn.prepareStatement("select * from books");
            rs = st.executeQuery();

            while(rs.next()){
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getBoolean("available")
                );
                books.add(book);
            }

            for(Book b : books){
                System.out.println(b.getTitle() + " - " + b.getAuthor());
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void addBook() throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        Scanner sc = new Scanner(System.in);
        try{
            System.out.println("BOOK TITLE : ");
            String title = sc.nextLine();
            System.out.println("BOOK author : ");
            String author = sc.nextLine();
            System.out.println("BOOK CATEGORY : ");
            String category = sc.nextLine();
            System.out.println("BOOK AVAILABILITY : ");
            int available = sc.nextInt();

            conn = DBConnection.connect();

            st = conn.prepareStatement("insert into books (title, author, category, available) values (?,?,?,?)");
            st.setString(1,title);
            st.setString(2,author);
            st.setString(3,category);
            st.setInt(4,available);

            int rs = st.executeUpdate();
            if(rs > 0){
                System.out.println("BOOK ADDED SUCCESSFULLY");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            if (st != null) st.close();
            if (conn != null) conn.close();
        }
    }

    public static void updateBook() throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        Scanner sc = new Scanner(System.in);

        try{
            System.out.println("BOOK ID TO UPDATE : ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.println("BOOK TITLE : ");
            String title = sc.nextLine();

            conn = DBConnection.connect();
            st = conn.prepareStatement("update books set title = ? where id = ?");
            st.setString(1,title);
            st.setInt(2,id);

            int rs = st.executeUpdate();

            if(rs > 0){
                System.out.println("BOOK UPDATED SUCCESSFULLY");
            }else{
                System.out.println("BOOK NOT UPDATED");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            if (st != null) st.close();
            if (conn != null) conn.close();
        }
    }

    public static void deleteBook() throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        Scanner sc = new Scanner(System.in);

        try{
            System.out.println("BOOK ID TO DELETE : ");
            int id = sc.nextInt();
            conn = DBConnection.connect();
            st = conn.prepareStatement("delete from books where id = ?");
            st.setInt(1,id);
            int rs = st.executeUpdate();
            if(rs > 0){
                System.out.println("BOOK DELETED SUCCESSFULLY");
            }else{
                System.out.println("BOOK FAILED DELETED");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            if (st != null) st.close();
            if (conn != null) conn.close();
        }
    }
}
