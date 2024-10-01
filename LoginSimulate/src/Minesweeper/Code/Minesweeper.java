package LoginSimulate.src.Minesweeper.Code;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Minesweeper extends JFrame {

    private JLabel statusbar;

    public Minesweeper() {
        initUI();
    }

    private void initUI() {
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        JButton playAgainBtn = new JButton("Play Again");

        Board board = new Board(statusbar, playAgainBtn);
        add(board);

        setResizable(false);

        add(playAgainBtn, BorderLayout.NORTH);

        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
