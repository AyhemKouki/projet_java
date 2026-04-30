package controller;

import database.DBConnection;

import java.sql.*;
import java.time.LocalDate;

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
}