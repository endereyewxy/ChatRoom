package chatroom.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class SingleChatWindow {
	
	public static void main(String[] args) {
		String user = "Mike";
		SingleChatWindow chat = new SingleChatWindow(user);
	}
	
	public SingleChatWindow(String user) {
		JFrame frame = new JFrame();
		
		frame.setTitle(user);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(3);
		frame.setLocationRelativeTo(null);
		frame.setFont(new Font("ËÎÌå",Font.PLAIN,14));
		
		JSplitPane splitPane1 = new JSplitPane();
		
		JScrollPane chatArea = new JScrollPane(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel inputArea = new JPanel(new BorderLayout());
		
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

		frame.add(splitPane1);
		frame.setVisible(true);
		
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}

		});
	}
}
