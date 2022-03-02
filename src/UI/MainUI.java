package UI;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
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
	private JButton btnStop, btnStart, btnBrowse, btnClearFeed, btnResetFrequencies, btnLog;
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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("keyIcon.png")));

		JLabel lblNewLabel = new JLabel("Select Source Folder");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(335, 10, 305, 37);
		frame.getContentPane().add(lblNewLabel);

		pathSelected = new JTextField();
		pathSelected.setBounds(335, 58, 198, 20);
		frame.getContentPane().add(pathSelected);
		pathSelected.setColumns(10);

		// ****FILE EXPLORER*****
		btnBrowse = new JButton("BROWSE");
		btnBrowse.setBounds(543, 58, 97, 20);
		frame.getContentPane().add(btnBrowse);
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
					btnLog.setEnabled(true);
				} catch (Exception e2) {
				}
			}
		});

		// ****START BUTTON****
		btnStart = new JButton("START");
		btnStart.setEnabled(false);
		btnStart.setBounds(335, 108, 89, 23);
		frame.getContentPane().add(btnStart);
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

		// ****STOP BUTTON***
		btnStop = new JButton("PAUSE");
		btnStop.setEnabled(false);
		btnStop.setBounds(444, 108, 89, 23);
		frame.getContentPane().add(btnStop);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ks.stop();
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
			}
		});

		// ****RESET BUTTON***
		btnResetFrequencies = new JButton("RESET FREQUENCIES");
		btnResetFrequencies.setBounds(392, 169, 190, 29);
		frame.getContentPane().add(btnResetFrequencies);
		btnResetFrequencies.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int sure = JOptionPane.showConfirmDialog(null, "Are you sure? It will delete all previous data!");
				if (sure == JOptionPane.YES_OPTION)
					op.destroy();
			}
		});

		// ****CLEAR FEED BUTTON***
		btnClearFeed = new JButton("CLEAR FEED");
		btnClearFeed.setBounds(392, 222, 190, 29);
		frame.getContentPane().add(btnClearFeed);
		btnClearFeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mFeed.setText("");
			}
		});

		// ****LOG BUTTON***
		btnLog = new JButton("LOG");
		btnLog.setBounds(551, 108, 89, 23);
		btnLog.setEnabled(false);
		frame.getContentPane().add(btnLog);
		btnLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop d = Desktop.getDesktop();
					d.open(new File(pathSelected.getText() + "//keyStrokes.txt"));
				} catch (Exception exp) {
				}
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
		model.addColumn("TIME");
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
