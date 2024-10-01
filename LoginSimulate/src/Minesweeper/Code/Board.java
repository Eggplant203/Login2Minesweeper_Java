package LoginSimulate.src.Minesweeper.Code;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import LoginSimulate.LoginApp;

import javax.swing.JButton;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

public class Board extends JPanel {

    private final int NUM_IMAGES = 13;
    private int CELL_SIZE = 30;
    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private final int N_MINES = 10;
    private final int N_ROWS = 9;
    private final int N_COLS = 9;

    private int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    private int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;

    private Instant startTime;
    private Instant endTime;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private Image[] img;
    private boolean nahIWin;
    private int midPress = 0;
    private int minutesAdded = 2;

    private int allCells;
    private final JLabel statusbar;
    private final JButton playAgainBtn;

    public Board(JLabel statusbar, JButton playAgainBtn) {
        this.statusbar = statusbar;
        this.playAgainBtn = playAgainBtn;
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        img = new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            var path = "LoginSimulate/src/Minesweeper/Pic/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();

        // "Play Again" button action
        playAgainBtn.setIcon(new ImageIcon("LoginSimulate/src/Minesweeper/Pic/again.png"));
        playAgainBtn.addActionListener(e -> {
            newGame();
            repaint();
        });
    }  

    private void newGame() {
        nahIWin = false;
        midPress = 0;
        startTime = Instant.now();

        int cell;

        var random = new Random();
        inGame = true;
        minesLeft = N_MINES;

        allCells = N_ROWS * N_COLS;
        field = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < N_MINES) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells)
                    && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % N_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - N_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + N_COLS - 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - N_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                cell = position + N_COLS;
                if (cell < allCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (N_COLS - 1)) {
                    cell = position - N_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + N_COLS + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }

    private void find_empty_cells(int j) {

        int current_col = j % N_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - N_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS - 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - N_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + N_COLS;
        if (cell < allCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (N_COLS - 1)) {
            cell = j - N_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + N_COLS + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Đừng quên gọi phương thức cha
        int uncover = 0;
    
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                int cell = field[(i * N_COLS) + j];
    
                if (inGame && cell == MINE_CELL) {
                    inGame = false; // Ghi lại nếu mở mìn
                }
    
                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }
    
                } else {
                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
    
                g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), CELL_SIZE, CELL_SIZE, this);
            }
        }

        // If game won
        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText(midPress!=0 ? "Game won (but add " + Integer.toString(midPress * minutesAdded) + " minutes)" : "Game won");
            endTime = Instant.now();
            long timeElapsed = Duration.between(startTime, endTime).toMillis();
        
            // Read existing scores from the leaderboard
            ArrayList<ScoreEntry> existingScores = Leaderboard.readScoresFromFile();
            
            // Add the new score entry
            existingScores.add(new ScoreEntry(LoginApp.stoUsername + " (newest)", timeElapsed));
        
            // Sort scores if necessary
            Collections.sort(existingScores);
        
            // Save updated scores back to the file
            Leaderboard leaderboard = new Leaderboard(existingScores);
            leaderboard.saveScoresToFile(existingScores);
        
            // Show leaderboard after saving
            showLeaderboard(); // Hiển thị bảng điểm sau khi lưu
            Leaderboard.removeNewestSuffixFromUsernames();
            nahIWin = true;
        }        
        
        // If game lost
        else if (!inGame && !nahIWin) {
            endTime = Instant.now();
            statusbar.setText("Game lost");
        }
        
    }
    
    private void showLeaderboard() {
        ArrayList<ScoreEntry> scores = Leaderboard.readScoresFromFile();
        Leaderboard leaderboard = new Leaderboard(scores);
        leaderboard.setVisible(true);
    }
    
    
    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        
            if (!inGame) {
                return;
            }
        
            int x = e.getX();
            int y = e.getY();
        
            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;
        
            boolean doRepaint = false;
        
            if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {
        
                if (e.getButton() == MouseEvent.BUTTON3) {
        
                    if (field[(cRow * N_COLS) + cCol] > MINE_CELL) {
        
                        doRepaint = true;
        
                        if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {
        
                            if (minesLeft > 0) {
                                field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
                                statusbar.setText(midPress!=0 ? Integer.toString(minesLeft) + " (+" + Integer.toString(midPress) + ")" : Integer.toString(minesLeft));
                            } else {
                                statusbar.setText(midPress!=0 ? "No marks left" + " (+" + Integer.toString(midPress) + ")" : "No marks left");
                            }
                        } else {
        
                            field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;
                            minesLeft++;
                            statusbar.setText(midPress!=0 ? Integer.toString(minesLeft) + " (+" + Integer.toString(midPress) + ")" : Integer.toString(minesLeft));
                        }
                    }

                } else if (e.getButton() == MouseEvent.BUTTON2) { // Nút chuột giữa
                    startTime = startTime.minus(Duration.ofMinutes(minutesAdded));
                    midPress++;
                    minesLeft++;
                    statusbar.setText(Integer.toString(minesLeft) + " (+" + Integer.toString(midPress) + ")");       
                } else {
        
                    if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {
                        return;
                    }
        
                    if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
                            && (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {
        
                        field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
                        doRepaint = true;
        
                        if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
                            inGame = false;
                        }
        
                        if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL) {
                            find_empty_cells((cRow * N_COLS) + cCol);
                        }
                    }
                }
        
                if (doRepaint) {
                    repaint();
                }
            }
        }        
    }
}