import asciiPanel.AsciiPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame implements KeyListener {
    private static final long serialVersionUID = 1L;

    private AsciiPanel terminalPanel;
    private Screen screen;

    public Main(){
        super();
        terminalPanel = new AsciiPanel();
        add(terminalPanel);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
    }
    public void repaint(){
        terminalPanel.clear();
        screen.displayOutput(terminalPanel);
        super.repaint();
    }
    public void keyPressed(KeyEvent event){
        screen = screen.respondToUserInput(event);
        repaint();
    }
    public void keyReleased(KeyEvent event){}
    public void keyTyped(KeyEvent event){}

    public static void main(String[] args) {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        app.setResizable(false);
    }


}
