package controller;

import database.DBConnection;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import util.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    // ================= ADD USER =================
    public static boolean addUser(String name, String email, String password, String role) {

        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            // HASH PASSWORD
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            st.setString(1, name);
            st.setString(2, email);
            st.setString(3, hashedPassword);
            st.setString(4, role);

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // ================= LIST USERS =================
    public static List<User> listUsers() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql);
                ResultSet rs = st.executeQuery()
        ) {

            while (rs.next()) {

                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    // ================= UPDATE USER =================
    public static boolean updateUser(int id, String name, String email, String password) {

        String sql;

        // IF PASSWORD EMPTY -> KEEP OLD PASSWORD
        if (password == null || password.isEmpty()) {
            sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        } else {
            sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
        }

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setString(1, name);
            st.setString(2, email);

            // WITHOUT PASSWORD UPDATE
            if (password == null || password.isEmpty()) {

                st.setInt(3, id);

            } else {

                // HASH NEW PASSWORD
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                st.setString(3, hashedPassword);
                st.setInt(4, id);
            }

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // ================= DELETE USER =================
    public static boolean deleteUser(int id) {

        String sql = "DELETE FROM users WHERE id = ?";

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

    // ================= LOGIN =================
    public static boolean login(String email, String password) {

        String sql = "SELECT id, role, password FROM users WHERE email = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setString(1, email);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                String hashedPassword = rs.getString("password");

                // VERIFY PASSWORD
                if (BCrypt.checkpw(password, hashedPassword)) {

                    Session.role = rs.getString("role");
                    Session.userId = rs.getInt("id");

                    System.out.println("LOGIN SUCCESS");
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    // ================= GET USER BY ID =================
    public static User getUserById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (
                Connection conn = DBConnection.connect();
                PreparedStatement st = conn.prepareStatement(sql)
        ) {

            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}