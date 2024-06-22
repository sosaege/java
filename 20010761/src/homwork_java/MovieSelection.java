package homwork_java;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class MovieSelection extends JFrame {
	String user = "user1";
    String password = "user1";
	int ticketNum2;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public MovieSelection(int ticketNum) {
    	this.ticketNum2 = ticketNum;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("영화 선택");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        JLabel titleLabel = new JLabel("영화선택");
        labelPanel.add(titleLabel);
        frame.add(labelPanel, BorderLayout.NORTH);

        String[] columnNames = {"영화번호", "영화명", "감독명", "배우명", "장르", "개봉일"};
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

        JButton changeScheduleButton = new JButton("예매변경");
        changeScheduleButton.addActionListener(new ChangeScheduleButtonListener());
        controlPanel.add(changeScheduleButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        loadMovieData();

        frame.setVisible(true);
    }

    private void loadMovieData() {
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT 영화번호, 영화명, 감독명, 배우명, 장르, 개봉일 FROM 영화";
            try (ResultSet rs = stmt.executeQuery(query)) {
                tableModel.setRowCount(0);

                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("영화번호"),
                        rs.getString("영화명"),
                        rs.getString("감독명"),
                        rs.getString("배우명"),
                        rs.getString("장르"),
                        rs.getDate("개봉일")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private class ChangeScheduleButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int movieNumber = (int) tableModel.getValueAt(selectedRow, 0);
                int[] scheduleNumbers = getScheduleNumbers(movieNumber);
                new ticket2(ticketNum2, scheduleNumbers);
                frame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "영화를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }

        private int[] getScheduleNumbers(int movieNumber) {
            ArrayList<Integer> scheduleNumbersList = new ArrayList<>();
            String url = "jdbc:mysql://localhost:3306/db1";
            

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                String query = "SELECT 상영일정번호 FROM 상영일정 WHERE 영화_영화번호 = " + movieNumber;
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        scheduleNumbersList.add(rs.getInt("상영일정번호"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            int[] scheduleNumbers = new int[scheduleNumbersList.size()];
            for (int i = 0; i < scheduleNumbersList.size(); i++) {
                scheduleNumbers[i] = scheduleNumbersList.get(i);
            }

            return scheduleNumbers;
        }
    }
}
