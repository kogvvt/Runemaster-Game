import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class WinScreen implements Screen {
    @Override
    public void displayOutput(AsciiPanel asciiPanel) {
        asciiPanel.writeCenter("You found the sacred rune - Fehu! Congratulations!", 10);
        asciiPanel.writeCenter("Press [enter] to restart",22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
