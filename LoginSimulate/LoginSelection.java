package LoginSimulate;
import LoginSimulate.src.Minesweeper.Code.Minesweeper;
public class LoginSelection {

    public static void Minesweeper() {
        Minesweeper game = new Minesweeper();
        game.setVisible(true);
    }

    public static void Account() {
        Account account = new Account();
        account.showGUI();
    }

    // Method to view accounts
    public static void xemTaiKhoan() {
        Account();
    }
}