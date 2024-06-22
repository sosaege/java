package homwork_java;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ticket extends JFrame {
	String user = "user1";
    String password = "user1";
    int person = 1;
    int price = 15000;
    Container c = getContentPane();

    JLabel l5 = new JLabel("결제 금액 :  " + price);
    JLabel personL = new JLabel(Integer.toString(person));
    JComboBox<String> Ctable1;
    JComboBox<String> Ctable2;
    JComboBox<String> Ctable3;
    
    ticket(int[] scheduleNumbers) {
        c.setLayout(null);

        String paymentM[] = { "현금", "카드" };

        ArrayList<String> dateList = fetchScheduleDates(scheduleNumbers);
        Ctable1 = new JComboBox<>(dateList.toArray(new String[0]));
        Ctable2 = new JComboBox<>();
        Ctable3 = new JComboBox<>(paymentM);
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");
        JButton Cb = new JButton("예매");

        JLabel l1 = new JLabel("상영시작일");
        JLabel l2 = new JLabel("인원");
        JLabel l3 = new JLabel("결제 수단");
        JLabel l4 = new JLabel("상영시작시간");

        Cb.setBounds(70, 180, 120, 30);
        l1.setBounds(30, 30, 120, 20);
        l2.setBounds(30, 60, 120, 20);
        l3.setBounds(30, 120, 120, 20);
        l4.setBounds(30, 90, 120, 20);
        l5.setBounds(70, 150, 200, 20);
        Ctable1.setBounds(150, 30, 120, 20);
        personL.setBounds(100, 60, 45, 20);
        plus.setBounds(130, 60, 45, 20);
        minus.setBounds(180, 60, 45, 20);
        Ctable2.setBounds(150, 90, 120, 20);
        Ctable3.setBounds(150, 120, 120, 20);

        c.add(Cb);
        c.add(l1);
        c.add(l2);
        c.add(l3);
        c.add(l4);
        c.add(l5);
        c.add(Ctable1);
        c.add(plus);
        c.add(minus);
        c.add(personL);
        c.add(Ctable2);
        c.add(Ctable3);

        plus.addActionListener(new BtListener3());
        minus.addActionListener(new BtListener3());
        Cb.addActionListener(new BtListener3());
        Ctable1.addActionListener(new DateSelectionListener());

        l5.setVisible(true);

        setSize(400, 300);
        setVisible(true);
        setTitle("예매");
    }

    private ArrayList<String> fetchScheduleDates(int[] scheduleNumbers) {
        ArrayList<String> dateList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            for (int scheduleNumber : scheduleNumbers) {
                String query = "SELECT DISTINCT 상영시작일 FROM 상영일정 WHERE 상영일정번호 = " + scheduleNumber;
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        dateList.add(rs.getString("상영시작일"));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dateList;
    }

    private ArrayList<String> fetchScheduleTimes(String selectedDate) {
        ArrayList<String> timeList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/db1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT 상영시작시간 FROM 상영일정 WHERE 상영시작일 = '" + selectedDate + "'";
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    timeList.add(rs.getString("상영시작시간"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return timeList;
    }

    class DateSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedDate = (String) Ctable1.getSelectedItem();
            ArrayList<String> timeList = fetchScheduleTimes(selectedDate);
            Ctable2.removeAllItems();
            for (String time : timeList) {
                Ctable2.addItem(time);
            }
        }
    }

    class BtListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();

            if (b.getText().equals("+")) {
                if (person < 3) {
                    person++;
                    price = person * 15000;
                    personL.setText(person + "");
                    l5.setText("결제 금액 :  " + price);
                }
            } else if (b.getText().equals("-")) {
                if (person > 1) {
                    person--;
                    price = person * 15000;
                    personL.setText(person + "");
                    l5.setText("결제 금액 :  " + price);
                }
            } else if (b.getText().equals("예매")) {
                if (person <= 0) {
                    JOptionPane.showMessageDialog(ticket.this, "인원수가 0입니다. 인원수를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selectedTime = (String) Ctable2.getSelectedItem();
                String paymentMethod = (String) Ctable3.getSelectedItem();
                String selectedDate = (String) Ctable1.getSelectedItem();
                
                if (selectedTime == null) {
                    JOptionPane.showMessageDialog(ticket.this, "상영 시간을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int scheduleNumber = getScheduleNumber(selectedDate, selectedTime);

                new seat(scheduleNumber, person, paymentMethod);
                ticket.this.setVisible(false);
            }
        }

        private int getScheduleNumber(String selectedDate, String selectedTime) {
            int scheduleNumber = -1;
            String url = "jdbc:mysql://localhost:3306/db1";
            
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                String query = "SELECT 상영일정번호 FROM 상영일정 WHERE 상영시작일 = '" + selectedDate + "' AND 상영시작시간 = '" + selectedTime + "'";
                try (ResultSet rs = stmt.executeQuery(query)) {
                    if (rs.next()) {
                        scheduleNumber = rs.getInt("상영일정번호");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return scheduleNumber;
        }
    }
}
