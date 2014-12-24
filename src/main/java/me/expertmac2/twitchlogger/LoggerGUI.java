package me.expertmac2.twitchlogger;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class LoggerGUI extends JFrame {

	public static LoggerGUI instance;
	private JPanel contentPane;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JLabel lblWaiting;
	private JButton btnStopLogger;
	
	/**
	 * Create the frame.
	 */
	public LoggerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblWaiting = new JLabel("Cleaning up, please wait...");
		lblWaiting.setBounds(283, 243, 149, 14);
		lblWaiting.setVisible(false);
		contentPane.add(lblWaiting);
		
		btnStopLogger = new JButton("Stop Logger");
		btnStopLogger.setBounds(10, 239, 129, 23);
		btnStopLogger.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnStopLogger.setEnabled(false);
				lblWaiting.setVisible(true);
				TwitchLogger.instance.stopLogging();
			}
			
		});
		contentPane.add(btnStopLogger);
		
		JButton button = new JButton("Clear TextArea");
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				clearTextArea();
			}
			
		});
		button.setBounds(149, 239, 124, 23);
		contentPane.add(button);
		
		textArea = new JTextArea();
		textArea.setEditable(false);

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 11, 422, 210);
		contentPane.add(scrollPane);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				clearTextArea();
			}
			
		}, 0L, TwitchLogger.instance.getClearInterval());
		
		instance = this;
	}
	
	public void addToPane(String str) {
		textArea.setText(textArea.getText() + str + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	public void clearTextArea() {
		textArea.setText("Cleared TextArea.\n");
	}
	
}
