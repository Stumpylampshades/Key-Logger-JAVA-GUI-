package UI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jnativehook.GlobalScreen;

import utility.FileReadWrite;
import utility.keyStroke;

/**
 * @author avcbcoder last modified @21-Mar-2018 @12:47:03 AM Key Logger - TODO
 */

public class MainUI {

	private JFrame frame;
	private JTextField pathSelected;
	private JButton btnStop, btnStart, btnBrowse, btnClearFeed, btnResetFrequencies;
	private keyStroke ks;
	private JTextArea mFeed;
	private FileReadWrite op;
	private JTable jt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		// Register the keyListener
		try {
			GlobalScreen.registerNativeHook();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to register Key Listener");
		}

		// Launch UI
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
		frame.setTitle("KEY LOGGER");

		JLabel lblNewLabel = new JLabel("Directory Path");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(335, 10, 305, 37);
		frame.getContentPane().add(lblNewLabel);

		pathSelected = new JTextField();
		pathSelected.setBounds(335, 58, 198, 20);
		frame.getContentPane().add(pathSelected);
		pathSelected.setColumns(10);

		// ****FILE EXPLORER*****
		btnBrowse = new JButton("BROWSE");
		btnBrowse.setBounds(543, 58, 97, 20);
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(true);
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				fc.setDialogTitle("Choose directory");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				try {
					if (fc.showOpenDialog(btnBrowse) == JFileChooser.APPROVE_OPTION) {
					}
					pathSelected.setText(fc.getSelectedFile().getAbsolutePath());
					btnStart.setEnabled(true);
				} catch (Exception e2) {
				}
			}
		});
		frame.getContentPane().add(btnBrowse);

		// ****START BUTTON****
		btnStart = new JButton("START");
		btnStart.setEnabled(false);
		btnStart.setBounds(366, 108, 89, 23);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					op = new FileReadWrite(pathSelected.getText(), jt);
					ks = new keyStroke(pathSelected.getText(), op, mFeed);
					GlobalScreen.getInstance().addNativeKeyListener(ks);
					ks.start();
					btnStop.setEnabled(true);
					btnStart.setEnabled(false);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Invalid Path");
				}
			}
		});
		frame.getContentPane().add(btnStart);

		// ****STOP BUTTON***
		btnStop = new JButton("PAUSE");
		btnStop.setEnabled(false);
		btnStop.setBounds(519, 108, 89, 23);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ks.stop();
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
			}
		});
		frame.getContentPane().add(btnStop);

		btnResetFrequencies = new JButton("RESET FREQUENCIES");
		btnResetFrequencies.setBounds(392, 169, 190, 29);
		btnResetFrequencies.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				op.destroy();
			}
		});
		frame.getContentPane().add(btnResetFrequencies);

		btnClearFeed = new JButton("CLEAR FEED");
		btnClearFeed.setBounds(392, 222, 190, 29);
		frame.getContentPane().add(btnClearFeed);
		btnClearFeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mFeed.setText("");
			}
		});

		mFeed = new JTextArea();
		mFeed.setEditable(false);
		mFeed.setLineWrap(true);
		JScrollPane scrollFeed = new JScrollPane(mFeed);
		scrollFeed.setBounds(10, 307, 630, 91);
		frame.getContentPane().add(scrollFeed);

		JLabel lblFrequencies = new JLabel("FREQUENCIES");
		lblFrequencies.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblFrequencies.setBounds(10, 10, 305, 37);
		frame.getContentPane().add(lblFrequencies);

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("KEY");
		model.addColumn("HITS");
		jt = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(jt);
		scrollPane.setBounds(10, 54, 305, 197);
		frame.getContentPane().add(scrollPane);

		JLabel lblFeed = new JLabel("FEED");
		lblFeed.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFeed.setBounds(10, 262, 630, 34);
		frame.getContentPane().add(lblFeed);

	}

}
