package controller;

import database.DBConnection;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowController {

    // ================= BORROW ITEM =================
    public static boolean borrowItem(int userId, int itemId) {

        String sql = "INSERT INTO borrowed_items (user_id, item_id, borrow_date, return_date) VALUES (?, ?, ?, NULL)";
        String update = "UPDATE library_items SET available = 0 WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st1 = conn.prepareStatement(sql);
                PreparedStatement st2 = conn.prepareStatement(update)
        ) {

            String today = LocalDate.now().toString();

            st1.setInt(1, userId);
            st1.setInt(2, itemId);
            st1.setString(3, today);
            st1.executeUpdate();

            st2.setInt(1, itemId);
            st2.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("BORROW ERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= RETURN ITEM =================
    public static boolean returnItem(int borrowId, int itemId) {

        String sql = "UPDATE borrowed_items SET return_date = ? WHERE id = ?";
        String update = "UPDATE library_items SET available = 1 WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st1 = conn.prepareStatement(sql);
                PreparedStatement st2 = conn.prepareStatement(update)
        ) {

            String today = LocalDate.now().toString();

            st1.setString(1, today);
            st1.setInt(2, borrowId);
            st1.executeUpdate();

            st2.setInt(1, itemId);
            st2.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("RETURN ERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= GET BORROW ID =================
    public static int getBorrowId(int userId, int itemId) {

        String sql = "SELECT id FROM borrowed_items WHERE user_id = ? AND item_id = ? AND return_date IS NULL";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setInt(1, userId);
            st.setInt(2, itemId);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("GET BORROW ID ERROR: " + e.getMessage());
        }

        return -1;
    }

    // ================= GET USER BORROWED ITEMS =================
    public static List<LibraryItem> getUserBorrowedItems(int userId) {

        List<LibraryItem> items = new ArrayList<>();

        String sql = """
            SELECT li.* 
            FROM library_items li
            JOIN borrowed_items bi ON li.id = bi.item_id
            WHERE bi.user_id = ? AND bi.return_date IS NULL
        """;

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                String type = rs.getString("type").toUpperCase();

                if (ItemType.valueOf(type) == ItemType.BOOK) {

                    items.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category"),
                            rs.getBoolean("available"),
                            rs.getString("image_path")
                    ));

                } else if (ItemType.valueOf(type) == ItemType.MAGAZINE) {

                    items.add(new Magazine(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("issue_number"),
                            rs.getBoolean("available"),
                            rs.getString("image_path")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("GET BORROWED ITEMS ERROR: " + e.getMessage());
        }

        return items;
    }
}