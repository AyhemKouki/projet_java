package controller;

import database.DBConnection;
import model.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowController {

    // ================= BORROW BOOK =================
    public static boolean borrowBook(int userId, int bookId) {

        String sql = "INSERT INTO borrowed_books (user_id, book_id, borrow_date, return_date) VALUES (?, ?, ?, NULL)";
        String updateBook = "UPDATE books SET available = 0 WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st1 = conn.prepareStatement(sql);
                PreparedStatement st2 = conn.prepareStatement(updateBook)
        ) {

            String today = LocalDate.now().toString();

            // insert borrow record
            st1.setInt(1, userId);
            st1.setInt(2, bookId);
            st1.setString(3, today);
            st1.executeUpdate();

            // mark book unavailable
            st2.setInt(1, bookId);
            st2.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // ================= RETURN BOOK =================
    public static boolean returnBook(int borrowId, int bookId) {

        String sql = "UPDATE borrowed_books SET return_date = ? WHERE id = ?";
        String updateBook = "UPDATE books SET available = 1 WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st1 = conn.prepareStatement(sql);
                PreparedStatement st2 = conn.prepareStatement(updateBook)
        ) {

            String today = LocalDate.now().toString();

            st1.setString(1, today);
            st1.setInt(2, borrowId);
            st1.executeUpdate();

            st2.setInt(1, bookId);
            st2.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static int getBorrowId(int userId, int bookId) {

        String sql = "SELECT id FROM borrowed_books WHERE user_id = ? AND book_id = ? AND return_date IS NULL";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {
            st.setInt(1, userId);
            st.setInt(2, bookId);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ================= GET USER BORROWED BOOKS =================
    public static List<Book> getUserBorrowedBooks(int userId) {

        List<Book> books = new ArrayList<>();

        String sql = """
        SELECT b.* FROM books b
        JOIN borrowed_books bb ON b.id = bb.book_id
        WHERE bb.user_id = ? AND bb.return_date IS NULL
    """;

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

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
            e.printStackTrace();
        }

        return books;
    }
}