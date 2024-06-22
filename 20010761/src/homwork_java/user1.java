package homwork_java;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class user1 {
	String user = "user1";
    String password = "user1";
    private JFrame frame;
    private JTextField titleField;
    private JTextField directorField;
    private JTextField actorField;
    private JTextField genreField;
    private JTable table;
    private DefaultTableModel tableModel;

    public user1() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Movie Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 1, 5, 5));

        JLabel l1 = new JLabel("영화명 :  ");
        JLabel l2 = new JLabel("감독명 :  ");
        JLabel l3 = new JLabel("배우명 :  ");
        JLabel l4 = new JLabel("장르 :  ");
        l1.setHorizontalAlignment(JLabel.RIGHT);
        l2.setHorizontalAlignment(JLabel.RIGHT);
        l3.setHorizontalAlignment(JLabel.RIGHT);
        l4.setHorizontalAlignment(JLabel.RIGHT);

        titleField = new JTextField();
        directorField = new JTextField();
        actorField = new JTextField();
        genreField = new JTextField();

        inputPanel.add(l1);
        inputPanel.add(titleField);
        inputPanel.add(l2);
        inputPanel.add(directorField);
        inputPanel.add(l3);
        inputPanel.add(actorField);
        inputPanel.add(l4);
        inputPanel.add(genreField);

        JButton searchButton = new JButton("조회");
        searchButton.addActionListener(new SearchButtonListener());
        inputPanel.add(searchButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Table with movie information
        String[] columnNames = {"영화번호", "영화명", "상영시간", "상영등급", "감독명", "배우명", "장르", "개봉일", "평점", "영화 소개"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton detailButton = new JButton("영화 소개");
        detailButton.addActionListener(new DetailButtonListener());
        controlPanel.add(detailButton);

        JButton bookButton = new JButton("예매");
        bookButton.addActionListener(new BookButtonListener());
        controlPanel.add(bookButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	
            String url = "jdbc:mysql://localhost:3306/db1";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                String query = "SELECT * FROM 영화 WHERE 1=1";
                if (!titleField.getText().trim().isEmpty()) {
                    query += " AND 영화명 LIKE '%" + titleField.getText().trim() + "%'";
                }
                if (!directorField.getText().trim().isEmpty()) {
                    query += " AND 감독명 LIKE '%" + directorField.getText().trim() + "%'";
                }
                if (!actorField.getText().trim().isEmpty()) {
                    query += " AND 배우명 LIKE '%" + actorField.getText().trim() + "%'";
                }
                if (!genreField.getText().trim().isEmpty()) {
                    query += " AND 장르 LIKE '%" + genreField.getText().trim() + "%'";
                }

                try (ResultSet rs = stmt.executeQuery(query)) {
                    tableModel.setRowCount(0); // Clear existing data

                    while (rs.next()) {
                        Object[] row = {
                            rs.getInt("영화번호"),
                            rs.getString("영화명"),
                            rs.getString("상영시간"),
                            rs.getString("상영등급"),
                            rs.getString("감독명"),
                            rs.getString("배우명"),
                            rs.getString("장르"),
                            rs.getDate("개봉일"),
                            rs.getFloat("평점"),
                            rs.getString("영화소개")
                        };
                        tableModel.addRow(row);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class DetailButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String movieTitle = (String) tableModel.getValueAt(selectedRow, 1);
                String movieDescription = (String) tableModel.getValueAt(selectedRow, 9);
                JOptionPane.showMessageDialog(frame, movieDescription, "영화 소개 - " + movieTitle, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "영화를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class BookButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int movieNumber = (int) tableModel.getValueAt(selectedRow, 0);
                ScheduleData[] scheduleDataList = fetchScheduleData(movieNumber);
                int[] scheduleNumbers = new int[scheduleDataList.length];
                for (int i = 0; i < scheduleDataList.length; i++) {
                    scheduleNumbers[i] = scheduleDataList[i].scheduleNumber;
                }
             
                new ticket(scheduleNumbers);
                frame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "영화를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }

        private ScheduleData[] fetchScheduleData(int movieNumber) {
            String url = "jdbc:mysql://localhost:3306/db1";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                String query = "SELECT 상영일정번호 FROM 상영일정 WHERE 영화_영화번호 = " + movieNumber;
                try (ResultSet rs = stmt.executeQuery(query)) {
                    ArrayList<ScheduleData> scheduleDataList = new ArrayList<>();
                    while (rs.next()) {
                        int scheduleNumber = rs.getInt("상영일정번호");
                        scheduleDataList.add(new ScheduleData(scheduleNumber));
                    }
                    return scheduleDataList.toArray(new ScheduleData[0]);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return new ScheduleData[0];
            }
        }
    }

    private static class ScheduleData {
        int scheduleNumber;

        ScheduleData(int scheduleNumber) {
            this.scheduleNumber = scheduleNumber;
        }

        @Override
        public String toString() {
            return String.valueOf(scheduleNumber);
        }
    }
}