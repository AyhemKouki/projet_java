package controller;

import database.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserController {
    public static void addUser() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("NAME: ");
        String name = sc.nextLine();

        System.out.print("EMAIL: ");
        String email = sc.nextLine();

        System.out.print("PASSWORD: ");
        String password = sc.nextLine();

        System.out.print("ROLE (admin/user): ");
        String role = sc.nextLine();

        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try{
            Connection conn = DBConnection.connect();
            PreparedStatement st = conn.prepareStatement(query);

            st.setString(1, name);
            st.setString(2, email);
            st.setString(3, password);
            st.setString(4, role);

            int rs = st.executeUpdate();

            if (rs > 0) {
                System.out.println("USER ADDED SUCCESSFULLY");
            }else{
                System.out.println("USER FAILED TO ADD");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM users";

        try{
            Connection conn = DBConnection.connect();
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User user =new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
                users.add(user);
            }

            for (User u : users) {
                System.out.println(u.getName() + " - " + u.getEmail() + " (" + u.getRole() + ")");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void updateUser() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("USER ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("NEW NAME: ");
        String name = sc.nextLine();

        String query = "UPDATE users SET name = ? WHERE id = ?";

        try{
            Connection conn = DBConnection.connect();
            PreparedStatement st = conn.prepareStatement(query);

            st.setString(1, name);
            st.setInt(2, id);

            int rs = st.executeUpdate();

            if (rs > 0) {
                System.out.println("USER UPDATED  SUCCESSFULLY");
            } else {
                System.out.println("USER NOT FOUND");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUser() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("USER ID TO DELETE: ");
        int id = sc.nextInt();

        String query = "DELETE FROM users WHERE id = ?";

        try{
            Connection conn = DBConnection.connect();
            PreparedStatement st = conn.prepareStatement(query);

            st.setInt(1, id);

            int rs = st.executeUpdate();

            if (rs > 0) {
                System.out.println("USER DELETED SUCCESSFULLY");
            } else {
                System.out.println("USER NOT FOUND");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static String login() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("EMAIL: ");
        String email = sc.nextLine();

        System.out.print("PASSWORD: ");
        String password = sc.nextLine();

        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try{
            Connection conn = DBConnection.connect();
            PreparedStatement st = conn.prepareStatement(query);

            st.setString(1, email);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("LOGIN SUCCESSFUL (" + role + ")");
                return role;
            } else {
                System.out.println("INVALID EMAIL OR PASSWORD");
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
