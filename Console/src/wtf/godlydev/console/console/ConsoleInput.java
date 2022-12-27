package wtf.godlydev.console.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsoleInput extends JTextArea implements KeyListener {

	private final JFrame frame = new JFrame();
	
	private int currentLength = 0;
	private String currentCommand = "";

	public ConsoleInput() {
		super(24, 80);
		setLineWrap(true);
		setBackground(Color.BLACK);
		setForeground(Color.LIGHT_GRAY);
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		addKeyListener(this);
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				append(String.valueOf((char) b));
			}
		}));
		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				append(String.valueOf((char) b));
			}
		}));
		JScrollPane scroll = new JScrollPane(this);

		// Add Textarea in to middle panel
		frame.add(scroll);
		
	}

	public void init() {
		frame.pack();
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getModifiers() == Toolkit.getDefaultToolkit ().getMenuShortcutKeyMaskEx()) {
			System.out.println("pog");
			e.consume();
		}
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if(this.currentLength == 0) {
				e.consume();
			} else {
				this.currentLength--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// process cmd
			this.append(this.currentCommand);
			this.currentCommand = "";
			this.currentLength = 0;
		} else {
			this.currentLength++;
			this.currentCommand += e.getKeyChar();
		}
		System.out.println("yeet");
		
	}

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }
	
	

}
