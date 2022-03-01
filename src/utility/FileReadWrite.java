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

/**
 * @author avcbcoder last modified @21-Mar-2018 @02:53:08 AM Key Logger - TODO
 */

public class FileReadWrite {
	private String freq, keyStrokes;
	private HashMap<String, Integer> keyMap;
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

	public void add(String s) { // This function adds to the keypress count stored
		try {
			ObjectInputStream inp = new ObjectInputStream(new FileInputStream(freq));
			HashMap<String, Integer> keys = (HashMap<String, Integer>) inp.readObject();
			if (keys.containsKey(s))
				keys.put(s, keys.get(s) + 1);
			else
				keys.put(s, 1);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(freq));
			out.writeObject(keys);
			out.close();
			inp.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		addTofile(s);
		resetTable();
	}

	/**
	 * RESET
	 */
	private void resetTable() { // Resets the table, as well as sorting it based on keypress count.
		deleteTable();
		HashMap<String, Integer> keys;
		DefaultTableModel model = (DefaultTableModel) jt.getModel();
		try {
			ObjectInputStream inp = new ObjectInputStream(new FileInputStream(freq));
			keys = (HashMap<String, Integer>) inp.readObject();
			ArrayList<ForSort> al = new ArrayList<>();
			for (String k : keys.keySet()) {
				al.add(new ForSort(k, keys.get(k)));
			}
			Collections.sort(al);
			for (ForSort f : al) {
				model.addRow(new Object[] { f.s, f.f });
			}
			inp.close();
		} catch (Exception e) {

		}
	}

	public class ForSort implements Comparable<ForSort> { // This class is For Sorting (ForSort) so that the arraylist is sorted by the number of keypresses. 
		String s;
		int f;

		public ForSort(String s, int f) {
			this.f = f;
			this.s = s;
		}

		@Override
		public int compareTo(ForSort o) {
			return o.f - this.f;
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
