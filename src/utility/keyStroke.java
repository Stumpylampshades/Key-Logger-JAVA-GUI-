package utility;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JTextArea;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import UI.MainUI;

/**
 * @author avcbcoder last modified @21-Mar-2018 @2:31:45 AM Key Logger - TODO
 */

public class keyStroke implements NativeKeyListener {

	private String lastKey = "";
	private FileReadWrite op;
	private boolean run;
	private long lastVisit;
	private JTextArea mFeed;
	private HashMap<Integer,TimeAndUp> pressTime = new HashMap<>();

	public keyStroke(String path, FileReadWrite op, JTextArea mFeed) {
		this.op = op;
		this.mFeed = mFeed;
		mFeed.setText("");
		lastVisit = System.currentTimeMillis();
	}

	public class TimeAndUp {
		long time;
		boolean pressed = false;
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		TimeAndUp tau;
		if(pressTime.containsKey(e.getKeyCode())) {
			tau = pressTime.get(e.getKeyCode());
			if(!tau.pressed) {
				tau.pressed = true;
				tau.time = e.getWhen();
				pressTime.put(e.getKeyCode(), tau);
			}
		}
		else {
			tau = new TimeAndUp();
			tau.pressed = true;
			tau.time = e.getWhen();
			pressTime.put(e.getKeyCode(), tau);
		}
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

		op.add(e.getKeyCode(), addTofeed);
		lastVisit = System.currentTimeMillis();
		this.lastKey = key;
		if (!addTofeed.equals("Shift"))
			mFeed.append(addTofeed);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		TimeAndUp tau = pressTime.get(arg0.getKeyCode());
		long timeUp = arg0.getWhen();
		long timeToAdd = timeUp - tau.time;
		op.addTime(arg0.getKeyCode(), timeToAdd);
		tau.pressed = false;
		pressTime.put(arg0.getKeyCode(), tau);
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
