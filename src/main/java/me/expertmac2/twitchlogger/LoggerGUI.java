package me.expertmac2.twitchlogger;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		
		final JLabel lblWaiting = new JLabel("Cleaning up, please wait...");
		lblWaiting.setBounds(158, 243, 274, 14);
		lblWaiting.setVisible(false);
		contentPane.add(lblWaiting);
		
		final JButton btnStopLogger = new JButton("Stop Logger");
		btnStopLogger.setBounds(10, 239, 138, 23);
		btnStopLogger.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnStopLogger.setEnabled(false);
				lblWaiting.setVisible(true);
				TwitchLogger.instance.stopBot();
				TwitchLogger.instance.logWriter.setKeepWriting(false);
				
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						if (TwitchLogger.instance.logWriter.isOkayToExit() && !TwitchLogger.instance.isBotConnected()) {
							System.exit(0);
						}
					}
					
				}, 0L, 1000L);
			}
			
		});
		contentPane.add(btnStopLogger);
		
		textArea = new JTextArea();
		//textArea.setBounds(10, 11, 422, 210);
		textArea.setEditable(false);

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 11, 422, 210);
		contentPane.add(scrollPane);
		
		instance = this;
	}
	
	public void addToPane(String str) {
		textArea.setText(textArea.getText() + str + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
