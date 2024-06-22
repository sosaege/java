package homwork_java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class seat {
	String user = "user1";
    String password = "user1";
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel seatPanel;
    private JButton[][] seats;
    private int rows;
    private int cols;
    private int people;
    private int selectedSeats = 0;
    private int scheduleNumber;
    private String paymentMethod;
    private int theaterNumber;

    public seat(int scheduleNumber, int people, String paymentMethod) {
        this.scheduleNumber = scheduleNumber;
        this.people = people;
        this.paymentMethod = paymentMethod;
        fetchSeatInformation();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Movie Theater Seat Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new BorderLayout());
        JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
        screenLabel.setOpaque(true);
        screenLabel.setBackground(Color.LIGHT_GRAY);
        screenLabel.setPreferredSize(new Dimension(800, 50));
        screenPanel.add(screenLabel, BorderLayout.CENTER);

        mainPanel.add(screenPanel, BorderLayout.NORTH);

        seatPanel = new JPanel();
        seatPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        for (int j = 0; j < cols; j++) {
            JLabel label = new JLabel(String.valueOf((char) ('A' + j)), SwingConstants.CENTER);
            gbc.gridx = j + 1;
            gbc.gridy = 0;
            seatPanel.add(label, gbc);
        }

        for (int i = 0; i < rows; i++) {
            JLabel rowLabel = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER);
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            seatPanel.add(rowLabel, gbc);
            for (int j = 0; j < cols; j++) {
                JButton seatButton = new JButton();
                seatButton.setPreferredSize(new Dimension(40, 40));
                seatButton.setBackground(Color.WHITE);
                seatButton.addActionListener(new SeatButtonListener(i, j));
                seats[i][j] = seatButton;
                gbc.gridx = j + 1;
                seatPanel.add(seatButton, gbc);
            }
        }

        updateSeatStatus();

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton bookButton = new JButton("예매");
        bookButton.addActionListener(new BookButtonListener());
        controlPanel.add(bookButton);

        mainPanel.add(seatPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void fetchSeatInformation() {
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT 상영관_상영관번호 FROM 상영일정 WHERE 상영일정번호 = " + scheduleNumber;
            
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    theaterNumber = rs.getInt("상영관_상영관번호");
                }
            }

            query = "SELECT 가로좌석수, 세로좌석수 FROM 상영관 WHERE 상영관번호 = " + theaterNumber;
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    cols = rs.getInt("가로좌석수");
                    rows = rs.getInt("세로좌석수");
                    seats = new JButton[rows][cols];
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateSeatStatus() {
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT 좌석번호, 좌석사용여부 FROM 좌석 WHERE 상영관_상영관번호 = " + theaterNumber;
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int seatNumber = rs.getInt("좌석번호");
                    boolean isSeatUsed = rs.getBoolean("좌석사용여부");

                    int adjustedSeatNumber = seatNumber % (theaterNumber * 1000);
                    int row = (adjustedSeatNumber - 1) / cols;
                    int col = (adjustedSeatNumber - 1) % cols;

                    if (row >= 0 && row < rows && col >= 0 && col < cols) {
                        if (isSeatUsed) {
                            seats[row][col].setBackground(Color.RED);
                            seats[row][col].setEnabled(false);
                        } else {
                            seats[row][col].setBackground(Color.WHITE);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateButtonStates() {
        boolean enable = selectedSeats < people;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (seats[i][j].getBackground() == Color.WHITE) {
                    seats[i][j].setEnabled(enable);
                }
            }
        }
    }

    private class SeatButtonListener implements ActionListener {
        private int row;
        private int col;

        public SeatButtonListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getBackground() == Color.WHITE) {
                button.setBackground(Color.GREEN);
                selectedSeats++;
            } else if (button.getBackground() == Color.GREEN) {
                button.setBackground(Color.WHITE);
                selectedSeats--;
            }
            updateButtonStates();
        }
    }

    private class BookButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedSeats == people) {
                int[] selectedSeatNumbers = new int[people];
                int index = 0;
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (seats[i][j].getBackground() == Color.GREEN) {
                            selectedSeatNumbers[index++] = (theaterNumber * 1000) + ((i * cols) + j + 1);
                        }
                    }
                }

                bookSeats(selectedSeatNumbers);
                JOptionPane.showMessageDialog(frame, "좌석이 성공적으로 예매되었습니다!");
                new user2();
                frame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "선택된 좌석 수가 맞지 않습니다. " + people + "개의 좌석을 선택해주세요.");
            }
        }

        private void bookSeats(int[] seatNumbers) {
            String url = "jdbc:mysql://localhost:3306/db1";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int totalAmount = 15000;

                for (int i = 0; i < seatNumbers.length; i++) {
                    String query = "SELECT MAX(예매번호) FROM 예매정보";
                    int reservationNumber = 1;
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        if (rs.next()) {
                            reservationNumber = rs.getInt(1) + 1;
                        }
                    }

                    query = "INSERT INTO 예매정보 (예매번호, 결제방법, 결제상태, 결제금액, 결제일자, 회원정보_회원아이디) VALUES (" +
                            reservationNumber + ", '" + paymentMethod + "', 1, " + totalAmount + ", '" + currentDate + "', '" + user + "')";
                    stmt.executeUpdate(query);

                    query = "SELECT MAX(티켓번호) FROM 티켓";
                    int ticketNumber = 1;
                    try (ResultSet rs = stmt.executeQuery(query)) {
                        if (rs.next()) {
                            ticketNumber = rs.getInt(1) + 1;
                        }
                    }

                    int seatNumber = seatNumbers[i];
                    query = "INSERT INTO 티켓 (티켓번호, 발권여부, 표준가격, 판매가격, 좌석_좌석번호, 상영관_상영관번호, 상영일정_상영일정번호, 예매정보_예매번호) VALUES (" +
                            ticketNumber + ", 1, 15000, 15000, " + seatNumber + ", " + theaterNumber + ", " + scheduleNumber + ", " + reservationNumber + ")";
                    stmt.executeUpdate(query);

                    String query2 = "UPDATE 좌석 SET 좌석사용여부 = 1 WHERE 좌석번호 = " + seatNumber;
                    stmt.executeUpdate(query2);

                    ticketNumber++;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
