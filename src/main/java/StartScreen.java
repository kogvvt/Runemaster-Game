import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class StartScreen implements Screen{

    @Override
    public void displayOutput(AsciiPanel asciiPanel) {
        asciiPanel.writeCenter("RuneMaster",10);
        asciiPanel.writeCenter("Start", 22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
