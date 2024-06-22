package homwork_java;

import javax.swing.*;
import java.awt.event.*;
public class Main{
    public static void main(String[] args) {
    JFrame f=new JFrame("로그인");
    JLabel l1=new JLabel("아이디");
    l1.setBounds(20,20, 80,30);
    JTextField text = new JTextField();
    text.setBounds(100,20, 100,30);
    JLabel l2=new JLabel("비밀번호");
    l2.setBounds(20,75, 80,30);    
    JPasswordField value = new JPasswordField(); 
    value.setBounds(100,75, 100,30);
    JButton b = new JButton("로그인"); 
    b.setBounds(100,120, 80,30);

    f.add(l1); f.add(text);     
    f.add(l2);  f.add(value); f.add(b); 
    f.setSize(300,250);
    f.setLayout(null);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    b.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		             String id = text.getText();
		             String password = new String(value.getPassword());
		             if (id.equals("user1") && password.equals("user1")) {
		            	 	JOptionPane.showMessageDialog(f, "유저 로그인");
							f.setVisible(false);
							new user();
						} 
		             else if (id.equals("root") && password.equals("1234")) {
		            	 	JOptionPane.showMessageDialog(f, "관리자 로그인");
							new admin();
							f.setVisible(false);
						}
		             else {
		            	 JOptionPane.showMessageDialog(f, "아이디 또는 비밀번호를 확인해주십시오.");
		             }
		             }
	    });
	}
}


 