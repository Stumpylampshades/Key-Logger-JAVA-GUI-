package UI;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author avcbcoder last modified @21-Mar-2018 @12:47:03 AM Key Logger - TODO
 */

public class MainUI {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frame.setBounds(100, 100, 666, 447);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Directory Path");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(335, 10, 305, 37);
		frame.getContentPane().add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(335, 58, 220, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("BROWSE");
		btnNewButton.setBounds(565, 58, 75, 20);
		frame.getContentPane().add(btnNewButton);

		JButton btnStart = new JButton("START");
		btnStart.setBounds(366, 108, 89, 23);
		frame.getContentPane().add(btnStart);

		JButton btnStop = new JButton("PAUSE");
		btnStop.setBounds(519, 108, 89, 23);
		frame.getContentPane().add(btnStop);

		JButton btnResetFrequencies = new JButton("RESET FREQUENCIES");
		btnResetFrequencies.setBounds(392, 169, 190, 29);
		frame.getContentPane().add(btnResetFrequencies);

		JButton btnClearFeed = new JButton("CLEAR FEED");
		btnClearFeed.setBounds(392, 222, 190, 29);
		frame.getContentPane().add(btnClearFeed);

		JTextArea mFeed = new JTextArea();
		mFeed.setBounds(10, 307, 630, 91);
		mFeed.setEditable(false);
		frame.getContentPane().add(mFeed);

		JLabel lblFrequencies = new JLabel("FREQUENCIES");
		lblFrequencies.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFrequencies.setBounds(10, 10, 305, 37);
		frame.getContentPane().add(lblFrequencies);

		String data[][] = {};
		String column[] = {};
		JTable jt = new JTable(data, column);

		JScrollPane scrollPane = new JScrollPane(jt);
		scrollPane.setBounds(10, 54, 305, 197);
		frame.getContentPane().add(scrollPane);

		JLabel lblFeed = new JLabel("FEED");
		lblFeed.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFeed.setBounds(10, 262, 630, 34);
		frame.getContentPane().add(lblFeed);

	}

}
