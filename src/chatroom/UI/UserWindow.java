package chatroom.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class UserWindow {
	
	public static void main(String[] args) {
		String username = "Jack"; 
		String[] users = {"A","B","C","D","E"}; 
		String[] groups = {"X","Y","Z","W","T"};
		UserWindow uw = new UserWindow(username, users, groups);
	}
	
	public UserWindow(String username, String[] users, String[] groups) {
		JFrame frame = new JFrame();
		
		JButton onlineUsers = new JButton("在线用户");
		JButton myGroups = new JButton("我的群组");
		JButton myMessage = new JButton("我的消息");
		JButton createGroup = new JButton("创建群组");
		
		JList usersList = new JList(users);
		JList GroupsList = new JList(groups);
		
		final CardLayout cardLayout = new CardLayout();
		
		JPanel panel1 = new JPanel(new GridLayout(1, 2));
		JPanel panel2 = new JPanel(new GridLayout(1, 2));
		final JPanel panel3 = new JPanel(cardLayout);
		
		JScrollPane scrollPane1 = new JScrollPane(usersList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane scrollPane2 = new JScrollPane(GroupsList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel1.add(onlineUsers);
		panel1.add(myGroups);
		panel2.add(myMessage);
		panel2.add(createGroup);
		panel3.add(scrollPane1);
		panel3.add(scrollPane2);
		
		frame.setTitle(username);
		frame.setSize(250, 600);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.add(panel1, BorderLayout.NORTH);
		frame.add(panel3, BorderLayout.CENTER);
		frame.add(panel2, BorderLayout.SOUTH);
		frame.setVisible(true);
		
		onlineUsers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.first(panel3);
			}
		});
		myGroups.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.last(panel3);
			}
		});
	}
}
