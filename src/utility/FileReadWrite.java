package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * @author avcbcoder last modified @21-Mar-2018 @02:53:08 AM Key Logger - TODO
 */

public class FileReadWrite {
	private String freq, keyStrokes;
	private HashMap<Integer, CountTimeAndKey> keyMap;
	private JTable jt;

	public FileReadWrite(String path, JTable jt) { // Sets the instance variables for the locations of the files needed.
		this.jt = jt;
		boolean present = false;
		File dir = new File(path);
		keyStrokes = dir.getAbsolutePath() + "//keyStrokes.txt";
		freq = dir.getAbsolutePath() + "//freq.txt";

		for (File f : dir.listFiles()) {
			if (f.getName().equals("freq.txt")) {
				present = true;
			}
		}

		if (!present) { // If the files are not present, creates them.
			keyMap = new HashMap<>();
			try {
				ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(freq));
				obj.writeObject(keyMap);
				obj.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

		public void add(int keyCode, String key) { // This function adds to the keypress count stored
		try {
			CountTimeAndKey ctk;
			ObjectInputStream inp = new ObjectInputStream(new FileInputStream(freq));
			HashMap<Integer, CountTimeAndKey> keys = (HashMap<Integer, CountTimeAndKey>) inp.readObject();
			if (keys.containsKey(keyCode)) {
				ctk = keys.get(keyCode);
				ctk.count++;
				keys.put(keyCode, ctk);
			}
			else {
				ctk = new CountTimeAndKey(key);
				keys.put(keyCode, ctk);
			}
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(freq));
			out.writeObject(keys);
			out.close();
			inp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		addTofile(key);
		resetTable();
	}

	public void addTime(int keyCode, long timeToAdd) {
		try {
			CountTimeAndKey ctk;
			ObjectInputStream inp = new ObjectInputStream(new FileInputStream(freq));
			HashMap<Integer, CountTimeAndKey> keys = (HashMap<Integer, CountTimeAndKey>) inp.readObject();
			ctk = keys.get(keyCode);
			ctk.time += timeToAdd;
			keys.put(keyCode, ctk);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(freq));
			out.writeObject(keys);
			out.close();
			inp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * RESET
	 */
	private void resetTable() { // Resets the table, as well as sorting it based on keypress count.
		deleteTable();
		HashMap<Integer, CountTimeAndKey> keys;
		DefaultTableModel model = (DefaultTableModel) jt.getModel();
		try {
			ObjectInputStream inp = new ObjectInputStream(new FileInputStream(freq));
			keys = (HashMap<Integer, CountTimeAndKey>) inp.readObject();
			ArrayList<CountTimeAndKey> al = new ArrayList<>();
			for (Integer k : keys.keySet()) {
				al.add(keys.get(k));
			}
			Collections.sort(al);
			for (CountTimeAndKey ctk : al) {
				model.addRow(new Object[] { ctk.key, ctk.count, String.format("%1$tQ", ctk.time.longValue()) });
			}
			inp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param key
	 */
	public void addTofile(String key) { // Adds to the file for tracking the keys typed as a long string.
		if(key.equals("Shift"))
			return;
		try {
			FileWriter fileWriter = new FileWriter(keyStrokes, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			if (key.equals("Space"))
				bufferedWriter.write(" ");
			else if (key.equals("Enter"))
				bufferedWriter.newLine();
			else
				bufferedWriter.write(key);

			bufferedWriter.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "unable to capture this key");
		}
	}
	
	/**
	 * Clear the current state of table
	 */
	public void deleteTable() {
		DefaultTableModel dm = (DefaultTableModel) jt.getModel();
		int rowCount = dm.getRowCount();
		// Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
			dm.removeRow(i);
		}
	}
	
	/**
	 * Compeletly erase all the data
	 */
	public void destroy(){
		deleteTable();
		keyMap = new HashMap<>();
		try {
			ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(freq));
			obj.writeObject(keyMap);
			obj.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
