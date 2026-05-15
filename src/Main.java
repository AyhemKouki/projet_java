import controller.BorrowController;
import database.DatabaseInitializer;
import view.LoginUI;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        //DatabaseInitializer.init();
        BorrowController.checkLateReturns();

        LoginUI.main(args);

    }
}
