import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {
    private World world;
    private int screenHeight = 80;
    private int screenWidth = 23;
    public Actor player;
    private List<String> messages = new ArrayList();
    private FogOfWar fov;
    private Screen subscreen;

    public PlayScreen(){
        generateWorld();
       // this.fov = new FogOfWar(this.world);
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
        this.world = (new WorldBuilder(90,32,5)).buildCaves().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top){
        for(int i=0;i<screenWidth;i++){
            for(int j=0;j<screenHeight;j++){
                int wx = i+left;
                int wy = j+top;
                int wz = 0;
                Actor actor = world.actorAtLocation(wx,wy);
                if(actor != null){
                    terminal.write(actor.getCharacter(), actor.x - left, actor.y - top);
                }else{
                    terminal.write(world.character(wx,wy,wz),i,j);
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
    /*
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

*/

    public Screen respondToUserInput(KeyEvent key) {
        int level = this.player.getLevel();
        if (this.subscreen != null) {
            this.subscreen = this.subscreen.respondToUserInput(key);
        } else {
            switch (key.getKeyCode()) {
                case 37:
                case 72:
                    this.player.moveBy(-1, 0, 0);
                    break;
                case 38:
                case 75:
                    this.player.moveBy(0, -1, 0);
                    break;
                case 39:
                case 76:
                    this.player.moveBy(1, 0, 0);
                    break;
                case 40:
                case 74:
                    this.player.moveBy(0, 1, 0);
                    break;
                case 66:
                    this.player.moveBy(-1, 1, 0);
                    break;
                case 78:
                    this.player.moveBy(1, 1, 0);
                    break;
                case 85:
                    this.player.moveBy(1, -1, 0);
                    break;
                case 89:
                    this.player.moveBy(-1, -1, 0);
                    break;
            }

            switch (key.getKeyChar()) {
                case ',':
                case 'g':
                    this.player.pickup();
                    break;
                case '<':
                    if (this.userIsTryingToExit()) {
                        return this.userExits();
                    }

                    this.player.moveBy(0, 0, -1);
                    break;
                case '>':
                    this.player.moveBy(0, 0, 1);
                    break;
            }
        }

        if (this.player.getLevel() > level) {
            //this.subscreen = new LevelUpScreen(this.player, this.player.level() - level);
        }

        if (this.subscreen == null) {
            this.world.update();
        }

        return (Screen)(this.player.getHp() < 1 ? new GameOver() : this);
    }

    private boolean userIsTryingToExit() {
        return this.player.z == 0 && this.world.tile(this.player.x, this.player.y, this.player.z) == Tile.STAIRS_UP;
    }

    private Screen userExits() {
        Item[] var4;
        int var3 = (var4 = this.player.getInventory().getItems()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            Item item = var4[var2];
            if (item != null && item.getName().equals("teddy bear")) {
                return new WinScreen();
            }
        }

        this.player.modifyHp(0);
        return new GameOver();
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
