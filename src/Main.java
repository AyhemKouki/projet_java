import database.DatabaseInitializer;
import view.LoginUI;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseInitializer.init();

        //BookController.addBook();
        //BookController.ListBooks();
        //BookController.deleteBook();
        //BookController.updateBook();

        //UserController.addUser();
        //UserController.listUsers();
        //UserController.updateUser();
        //UserController.deleteUser();

        LoginUI.main(args);

    }
}
