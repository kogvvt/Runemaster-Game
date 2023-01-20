import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {
    private World world;
    private int screenHeight;
    private int screenWidth;
    public Actor player;
    private List<String> messages;

    public PlayScreen(){
        screenWidth = 80;
        screenHeight = 23;
        messages = new ArrayList<String>();
        generateWorld();
        ActorFactory actorFactory = new ActorFactory(world);
        createActors(actorFactory);
    }

    private void createActors(ActorFactory actorFactory){
        player = actorFactory.newPlayer(messages);
        for(int i = 0; i <7; ++i){
            actorFactory.newEnemy();
        }
    }

    private void generateWorld(){
        world = new WorldBuilder(90,31)
                .buildCaves()
                .build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top){
        for(int i=0;i<screenWidth;i++){
            for(int j=0;j<screenHeight;j++){
                int wx = i+left;
                int wy = j+top;
                Actor actor = world.actorAtLocation(wx,wy);
                if(actor != null){
                    terminal.write(actor.getCharacter(), actor.x - left, actor.y - top);
                }else{
                    terminal.write(world.character(wx,wy),i,j);
                }
            }
        }
    }

    @Override
    public void displayOutput(AsciiPanel asciiPanel) {
        int left = getScrollingX();
        int top = getScrollingY();
        displayTiles(asciiPanel, left, top);
        asciiPanel.write(player.getCharacter(),player.x-left, player.y-top);
        String hpStat = String.format("%3d/%3d hp", player.getHp(), player.getMaxHp());
        asciiPanel.write(hpStat,1,23);
        displayMessages(asciiPanel,messages);
    }

    @Override
    public Screen respondToUserInput(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                player.moveBy(0, 1);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_Q:
                player.moveBy(-1, -1);
                break;
            case KeyEvent.VK_E:
                player.moveBy(1, -1);
                break;
            case KeyEvent.VK_Z:
                player.moveBy(-1, 1);
                break;
            case KeyEvent.VK_X:
                player.moveBy(1, 1);
                break;
        }
        world.update();
        return this;
    }


    public int getScrollingX(){
        return Math.max(0,Math.min(player.x - screenWidth/2, world.getWidth() - screenWidth));
    }

    public int getScrollingY(){
        return Math.max(0,Math.min(player.y - screenHeight/2, world.getHeight() - screenHeight));
    }

    private void displayMessages(AsciiPanel asciiPanel, List<String> messages){
        int top = screenHeight - messages.size();
        for(int i = 0; i<messages.size(); i++){
            asciiPanel.writeCenter(messages.get(i), top+i);
        }
        messages.clear();
    }



}
