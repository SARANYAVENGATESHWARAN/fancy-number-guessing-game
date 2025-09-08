import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FancyNumberGame extends JFrame {
    private int randomNumber, attemptsLeft, maxAttempts, score = 0, roundsWon = 0, roundsPlayed = 0;
    private Random random = new Random();

    private JLabel infoLabel, attemptsLabel, scoreLabel, roundLabel;
    private JTextField guessField;
    private JButton guessButton, newRoundButton;
    private JPanel mainPanel;

    public FancyNumberGame(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        setTitle(" Number Guessing Game ");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MAIN PANEL with gradient background
        mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.CYAN, getWidth(), getHeight(), Color.MAGENTA);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridLayout(7, 1, 5, 5));
        add(mainPanel);

        // INFO LABEL
        infoLabel = new JLabel("Guess a number between 1 and 100", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        infoLabel.setForeground(Color.WHITE);
        mainPanel.add(infoLabel);

        // Attempts
        attemptsLabel = new JLabel("Attempts left: " + maxAttempts, SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(attemptsLabel);

        // Score
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(scoreLabel);

        // Rounds
        roundLabel = new JLabel("Rounds Won: 0", SwingConstants.CENTER);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(roundLabel);

        // Input Field
        guessField = new JTextField();
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(guessField);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        guessButton = new JButton(" Guess");
        newRoundButton = new JButton(" New Round");
        newRoundButton.setEnabled(false);

        styleButton(guessButton, Color.GREEN);
        styleButton(newRoundButton, Color.ORANGE);

        buttonPanel.add(guessButton);
        buttonPanel.add(newRoundButton);
        mainPanel.add(buttonPanel);

        // Event Listeners
        guessButton.addActionListener(e -> checkGuess());
        newRoundButton.addActionListener(e -> startNewRound());

        startNewRound();
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void startNewRound() {
        randomNumber = random.nextInt(100) + 1;
        attemptsLeft = maxAttempts;
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        infoLabel.setText("Guess a number between 1 and 100");
        infoLabel.setForeground(Color.WHITE);
        guessField.setText("");
        guessButton.setEnabled(true);
        newRoundButton.setEnabled(false);
        roundsPlayed++;
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText().trim());
            attemptsLeft--;
            attemptsLabel.setText("Attempts left: " + attemptsLeft);

            if (guess == randomNumber) {
                infoLabel.setText(" Correct! The number was " + randomNumber);
                infoLabel.setForeground(Color.GREEN);
                roundsWon++;
                score += (attemptsLeft + 1);
                showRoundSummary(true);
                endRound();
            } else if (guess < randomNumber) {
                infoLabel.setText("⬇ Too low! Try again.");
                infoLabel.setForeground(Color.YELLOW);
            } else {
                infoLabel.setText("⬆ Too high! Try again.");
                infoLabel.setForeground(Color.ORANGE);
            }

            if (attemptsLeft <= 0 && guess != randomNumber) {
                infoLabel.setText(" Out of attempts! Number was " + randomNumber);
                infoLabel.setForeground(Color.RED);
                showRoundSummary(false);
                endRound();
            }

            scoreLabel.setText("Score: " + score);
            roundLabel.setText("Rounds Won: " + roundsWon);
            guessField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Please enter a valid number!");
        }
    }

    private void showRoundSummary(boolean won) {
        String message = won
                ? " Congratulations! You guessed it right!"
                : " Better luck next time!";
        JOptionPane.showMessageDialog(this,
                message + "\nScore: " + score + "\nRounds Won: " + roundsWon,
                "Round Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void endRound() {
        guessButton.setEnabled(false);
        newRoundButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String input = JOptionPane.showInputDialog("Enter max attempts per round:", "7");
            int maxAttempts = 7;
            try {
                maxAttempts = Integer.parseInt(input);
            } catch (NumberFormatException ignored) {}
            FancyNumberGame game = new FancyNumberGame(maxAttempts);
            game.setVisible(true);
        });
    }
}
