package controller;

import database.DBConnection;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryItemController {

    // ================= GET ALL ITEMS =================
    public static List<LibraryItem> getAllItems() {

        List<LibraryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM library_items";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()
        ) {

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
            System.out.println("GET ALL ERROR: " + e.getMessage());
        }

        return items;
    }

    // ================= ADD ITEM =================
    public static boolean addItem(LibraryItem item) {

        String sql = """
                INSERT INTO library_items
                (title, author, category, issue_number, available, image_path, type)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setString(1, item.getTitle());

            String author = null;
            String category = null;
            Integer issueNumber = null;
            String type = null;

            if (item instanceof Book book) {
                author = book.getAuthor();
                category = book.getCategory();
                type = "BOOK";
            }

            if (item instanceof Magazine mag) {
                issueNumber = mag.getIssueNumber();
                type = "MAGAZINE";
            }

            st.setString(2, author);
            st.setString(3, category);

            if (issueNumber != null)
                st.setInt(4, issueNumber);
            else
                st.setNull(4, Types.INTEGER);

            st.setBoolean(5, item.getAvailable());
            st.setString(6, item.getImagePath());
            st.setString(7, type);

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("ADD ERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= UPDATE ITEM =================
    public static boolean updateItem(LibraryItem item) {

        String sql = """
                UPDATE library_items
                SET title = ?, author = ?, category = ?, issue_number = ?,
                    available = ?, image_path = ?, type = ?
                WHERE id = ?
                """;

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setString(1, item.getTitle());

            String author = null;
            String category = null;
            Integer issueNumber = null;
            String type = null;

            if (item instanceof Book book) {
                author = book.getAuthor();
                category = book.getCategory();
                type = "BOOK";
            }

            if (item instanceof Magazine mag) {
                issueNumber = mag.getIssueNumber();
                type = "MAGAZINE";
            }

            st.setString(2, author);
            st.setString(3, category);

            if (issueNumber != null)
                st.setInt(4, issueNumber);
            else
                st.setNull(4, Types.INTEGER);

            st.setBoolean(5, item.getAvailable());
            st.setString(6, item.getImagePath());
            st.setString(7, type);
            st.setInt(8, item.getId());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("UPDATE ERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= DELETE ITEM =================
    public static boolean deleteItem(int id) {

        String sql = "DELETE FROM library_items WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("DELETE ERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= AVAILABLE ITEMS =================
    public static List<LibraryItem> getAvailableItems() {

        List<LibraryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM library_items WHERE available = 1";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()
        ) {

            while (rs.next()) {

                String type = rs.getString("type").toUpperCase();

                if (ItemType.valueOf(type) == ItemType.BOOK) {

                    items.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category"),
                            true,
                            rs.getString("image_path")
                    ));

                } else if (ItemType.valueOf(type) == ItemType.MAGAZINE) {

                    items.add(new Magazine(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("issue_number"),
                            true,
                            rs.getString("image_path")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("AVAILABLE ERROR: " + e.getMessage());
        }

        return items;
    }
}