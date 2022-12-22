import java.util.List;

public class PlayerAi extends ActorAi {
    private List<String> messages;
    public PlayerAi(Actor actor, List<String> messages) {
        super(actor);
        this.messages = messages;
    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        if(tile.isGround()){
            actor.x = x;
            actor.y = y;
        }
    }

    public void onNotify(String message) {
        messages.add(message);
    }

}
