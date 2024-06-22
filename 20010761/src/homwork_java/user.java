package homwork_java;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class user extends JFrame{
	public user() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = getContentPane();
		c.setLayout(null);
		JTextArea ta = new JTextArea(20, 80);
		JScrollPane sp = new JScrollPane(ta);

		JButton b1 = new JButton("영화 조회");
		JButton b2 = new JButton("예매 조회");

		b1.setBounds(30, 30, 180, 120);
		b2.setBounds(30, 180, 180, 120);
		c.add(b1);
		c.add(b2);
		class BtListener2 implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton) e.getSource();

				if (b.getText().equals("영화 조회")) {
					setVisible(false);
					new user1();
				} else if (b.getText().equals("예매 조회")) {
					setVisible(false);
					new user2();
				}
			}
		}
 
		b1.addActionListener(new BtListener2());
		b2.addActionListener(new BtListener2());

		setSize(255, 370);
		setVisible(true);
		setTitle("Login Page");

	}
}
