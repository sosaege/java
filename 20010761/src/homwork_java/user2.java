package homwork_java;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class user2 extends JFrame {
	String user = "user1";
    String password = "user1";
    private JFrame frame;
    private JTable mainTable;
    private DefaultTableModel mainTableModel;

    private JTable scheduleTable;
    private DefaultTableModel scheduleTableModel;
    private JTable theaterTable;
    private DefaultTableModel theaterTableModel;
    private JTable ticketTable;
    private DefaultTableModel ticketTableModel;

    public user2() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Movie Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton searchButton = new JButton("조회");
        searchButton.addActionListener(new SearchButtonListener());
        inputPanel.add(searchButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        String[] mainColumnNames = {"영화명", "상영일", "상영관번호", "좌석번호", "판매가격"};
        mainTableModel = new DefaultTableModel(mainColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mainTable = new JTable(mainTableModel);
        mainTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = mainTable.rowAtPoint(evt.getPoint());
                if (row >= 0 && row < mainTable.getRowCount()) {
                    int seatNumber = (int) mainTableModel.getValueAt(row, 3);
                    int theaterNumber = (int) mainTableModel.getValueAt(row, 2);
                    loadRelatedInformation(seatNumber, theaterNumber);
                }
            }
        });
        JScrollPane mainScrollPane = new JScrollPane(mainTable);
        frame.add(mainScrollPane, BorderLayout.CENTER);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(3, 1));

        scheduleTableModel = new DefaultTableModel(new String[]{"상영일정번호", "상영시작일", "상영요일", "상영회차", "상영시작시간", "영화_영화번호", "상영관_상영관번호"}, 0);
        scheduleTable = new JTable(scheduleTableModel);
        lowerPanel.add(new JScrollPane(scheduleTable));

        theaterTableModel = new DefaultTableModel(new String[]{"상영관번호", "상영관사용여부", "가로좌석수", "세로좌석수"}, 0);
        theaterTable = new JTable(theaterTableModel);
        lowerPanel.add(new JScrollPane(theaterTable));

        ticketTableModel = new DefaultTableModel(new String[]{"티켓번호", "발권여부", "표준가격", "판매가격", "좌석_좌석번호", "상영관_상영관번호", "상영일정_상영일정번호", "예매정보_예매번호"}, 0);
        ticketTable = new JTable(ticketTableModel);
        lowerPanel.add(new JScrollPane(ticketTable));

        frame.add(lowerPanel, BorderLayout.EAST);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton deleteButton = new JButton("예매 정보 삭제");
        deleteButton.addActionListener(new DeleteButtonListener());
        controlPanel.add(deleteButton);

        JButton modifyButton = new JButton("예매 변경");
        modifyButton.addActionListener(new ModifyButtonListener());
        controlPanel.add(modifyButton);

        JButton changeScheduleButton = new JButton("상영일정 변경");
        changeScheduleButton.addActionListener(new ChangeScheduleButtonListener());
        controlPanel.add(changeScheduleButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String url = "jdbc:mysql://localhost:3306/db1";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

                String query = "SELECT 영화.영화명, 상영일정.상영시작일, 티켓.상영관_상영관번호, 티켓.좌석_좌석번호, 티켓.판매가격 " +
                               "FROM 예매정보 " +
                               "JOIN 티켓 ON 예매정보.예매번호 = 티켓.예매정보_예매번호 " +
                               "JOIN 상영일정 ON 티켓.상영일정_상영일정번호 = 상영일정.상영일정번호 " +
                               "JOIN 영화 ON 상영일정.영화_영화번호 = 영화.영화번호 " +
                               "WHERE 예매정보.회원정보_회원아이디 = 'user1'";

                try (ResultSet rs = stmt.executeQuery(query)) {
                    mainTableModel.setRowCount(0); 

                    while (rs.next()) {
                        Object[] row = {
                            rs.getString("영화명"),
                            rs.getDate("상영시작일"),
                            rs.getInt("상영관_상영관번호"),
                            rs.getInt("좌석_좌석번호"),
                            rs.getInt("판매가격")
                        };
                        mainTableModel.addRow(row);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadRelatedInformation(int seatNumber, int theaterNumber) {
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            // Load schedule information
            String scheduleQuery = "SELECT * FROM 상영일정 WHERE 상영일정번호 = " +
                "(SELECT 상영일정_상영일정번호 FROM 티켓 WHERE 좌석_좌석번호 = " + seatNumber + " AND 상영관_상영관번호 = " + theaterNumber + ")";
            try (ResultSet rs = stmt.executeQuery(scheduleQuery)) {
                scheduleTableModel.setRowCount(0);
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("상영일정번호"),
                        rs.getDate("상영시작일"),
                        rs.getString("상영요일"),
                        rs.getInt("상영회차"),
                        rs.getTime("상영시작시간"),
                        rs.getInt("영화_영화번호"),
                        rs.getInt("상영관_상영관번호")
                    };
                    scheduleTableModel.addRow(row);
                }
            }

            String theaterQuery = "SELECT * FROM 상영관 WHERE 상영관번호 = " + theaterNumber;
            try (ResultSet rs = stmt.executeQuery(theaterQuery)) {
                theaterTableModel.setRowCount(0);
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("상영관번호"),
                        rs.getInt("상영관사용여부"),
                        rs.getInt("가로좌석수"),
                        rs.getInt("세로좌석수")
                    };
                    theaterTableModel.addRow(row);
                }
            }

            String ticketQuery = "SELECT * FROM 티켓 WHERE 좌석_좌석번호 = " + seatNumber + " AND 상영관_상영관번호 = " + theaterNumber;
            try (ResultSet rs = stmt.executeQuery(ticketQuery)) {
                ticketTableModel.setRowCount(0); 
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("티켓번호"),
                        rs.getBoolean("발권여부"),
                        rs.getInt("표준가격"),
                        rs.getInt("판매가격"),
                        rs.getInt("좌석_좌석번호"),
                        rs.getInt("상영관_상영관번호"),
                        rs.getInt("상영일정_상영일정번호"),
                        rs.getInt("예매정보_예매번호")
                    };
                    ticketTableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1) {
                int dialogResult = JOptionPane.showConfirmDialog(frame, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    int seatNumber = (int) mainTableModel.getValueAt(selectedRow, 3);
                    int theaterNumber = (int) mainTableModel.getValueAt(selectedRow, 2);
                    deleteReservation(seatNumber, theaterNumber);
                    mainTableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "예매 정보를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void deleteReservation(int seatNumber, int theaterNumber) {
            String url = "jdbc:mysql://localhost:3306/db1";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                String selectReservationQuery = "SELECT 예매정보_예매번호 FROM 티켓 WHERE 좌석_좌석번호 = " + seatNumber + " AND 상영관_상영관번호 = " + theaterNumber;
                int reservationNumber = -1;
                try (ResultSet rs = stmt.executeQuery(selectReservationQuery)) {
                    if (rs.next()) {
                        reservationNumber = rs.getInt("예매정보_예매번호");
                    }
                }

                if (reservationNumber != -1) {
                    String updateSeatQuery = "UPDATE 좌석 SET 좌석사용여부 = 0 WHERE 좌석번호 = " + seatNumber;
                    stmt.executeUpdate(updateSeatQuery);

                    String deleteTicketQuery = "DELETE FROM 티켓 WHERE 좌석_좌석번호 = " + seatNumber + " AND 상영관_상영관번호 = " + theaterNumber;
                    stmt.executeUpdate(deleteTicketQuery);

                    String deleteReservationQuery = "DELETE FROM 예매정보 WHERE 예매번호 = " + reservationNumber;
                    stmt.executeUpdate(deleteReservationQuery);

                    JOptionPane.showMessageDialog(frame, "예매 정보가 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class ModifyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1) {
                int seatNumber = (int) mainTableModel.getValueAt(selectedRow, 3);
                String movieTitle = (String) mainTableModel.getValueAt(selectedRow, 0);
                int ticketNumber = getTicketNumber(seatNumber);
                new MovieSelection(ticketNumber);
            } else {
                JOptionPane.showMessageDialog(frame, "예매 정보를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ChangeScheduleButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow != -1) {
                int seatNumber = (int) mainTableModel.getValueAt(selectedRow, 3);
                String movieTitle = (String) mainTableModel.getValueAt(selectedRow, 0);
                int ticketNumber = getTicketNumber(seatNumber);
                int[] scheduleNumbers = getScheduleNumbers(movieTitle);
                new ticket2(ticketNumber, scheduleNumbers);
            } else {
                JOptionPane.showMessageDialog(frame, "예매 정보를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getTicketNumber(int seatNumber) {
        int ticketNumber = -1;
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            String query = "SELECT 티켓번호 FROM 티켓 WHERE 좌석_좌석번호 = " + seatNumber;
            try (ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    ticketNumber = rs.getInt("티켓번호");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ticketNumber;
    }

    private int[] getScheduleNumbers(String movieTitle) {
        int[] scheduleNumbers = new int[0];
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            String query = "SELECT 상영일정번호 FROM 상영일정 " +
                           "JOIN 영화 ON 상영일정.영화_영화번호 = 영화.영화번호 " +
                           "WHERE 영화.영화명 = '" + movieTitle + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                scheduleNumbers = new int[getRowCount(rs)];
                int index = 0;
                while (rs.next()) {
                    scheduleNumbers[index++] = rs.getInt("상영일정번호");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return scheduleNumbers;
    }

    private int getRowCount(ResultSet rs) throws SQLException {
        rs.last();
        int rowCount = rs.getRow();
        rs.beforeFirst();
        return rowCount;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new user2());
    }
}