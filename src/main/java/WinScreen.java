import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class WinScreen implements Screen {
    @Override
    public void displayOutput(AsciiPanel asciiPanel) {
        asciiPanel.write("You won!", 1 ,1);
        asciiPanel.write("Press [enter] to restart",2,2);
    }

    @Override
    public Screen respondToUserInput(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
