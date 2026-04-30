package controller;

import database.DBConnection;
import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookController {

    // ================= GET ALL BOOKS =================
    public static List<Book> ListBooks() {

        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()
        ) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getBoolean("available")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return books;
    }

    // ================= ADD BOOK =================
    public static boolean addBook(String title, String author, String category, boolean available) {

        String sql = "INSERT INTO books (title, author, category, available) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setString(1, title);
            st.setString(2, author);
            st.setString(3, category);
            st.setBoolean(4, available);

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // ================= UPDATE BOOK =================
    public static boolean updateBook(int id, String title) {

        String sql = "UPDATE books SET title = ? WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setString(1, title);
            st.setInt(2, id);

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // ================= DELETE BOOK =================
    public static boolean deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setInt(1, id);

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}