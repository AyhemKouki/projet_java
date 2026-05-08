package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void init() {


        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE,
                password TEXT NOT NULL,
                role TEXT DEFAULT 'USER'
            );
        """;

        String createBorrowTable = """
                CREATE TABLE IF NOT EXISTS borrowed_books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    book_id INTEGER,
                    borrow_date TEXT,
                    return_date TEXT
                );
        """;


        String q = """
                ALTER TABLE library_items
                ALTER COLUMN author DROP NOT NULL;
                """;

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {

            //stmt.execute(createUsersTable);
            //stmt.execute(createBorrowTable);

            stmt.executeUpdate(q);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}