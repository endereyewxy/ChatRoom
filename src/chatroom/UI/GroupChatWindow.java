package chatroom.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GroupChatWindow {
	public static void main(String[] args) {
		String group = "My Class";
		GroupChatWindow chat = new GroupChatWindow(group);
		
	}
	public GroupChatWindow(String group) {
		JFrame frame = new JFrame();
		
		frame.setTitle(group);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setFont(new Font("ËÎÌå",Font.PLAIN,14));
		
		String[] users = {"Jack", "Mike", "Micheal"};
		
		JSplitPane splitPane1 = new JSplitPane();
		JSplitPane splitPane2 = new JSplitPane();
		
		JScrollPane chatArea = new JScrollPane(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel inputArea = new JPanel(new BorderLayout());
		
		JList userList = new JList(users);
		
		JTextArea chatText = new JTextArea();
		JTextArea inputText = new JTextArea();

		JButton send = new JButton();
		
		chatText.setLineWrap(true);
		chatText.setEditable(false);
		inputText.setLineWrap(true);
		
		send.setText("·¢ËÍ");
		send.setFont(new Font("ËÎÌå",Font.PLAIN,14));
		
		chatArea.setViewportView(chatText);
		
		inputArea.add(inputText, BorderLayout.CENTER);
		inputArea.add(send, BorderLayout.EAST);
		
		splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane1.setContinuousLayout(true);
		splitPane1.setLeftComponent(chatArea);
		splitPane1.setRightComponent(inputArea);
		splitPane1.setDividerLocation(400);
		
		splitPane2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane2.setContinuousLayout(true);
		splitPane2.setLeftComponent(splitPane1);
		splitPane2.setRightComponent(userList);
		splitPane2.setDividerLocation(600);
		
		frame.add(splitPane2);
		frame.setVisible(true);
		
	}
}

