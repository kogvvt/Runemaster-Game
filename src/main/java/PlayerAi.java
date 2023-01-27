import java.util.List;

public class PlayerAi extends ActorAi {
    private List<String> messages;
    public PlayerAi(Actor actor, List<String> messages) {
        super(actor);
        this.messages = messages;
    }

    @Override
    public void onEnter(int x, int y, int z, Tile tile) {
        if(tile.isGround()){
            this.actor.x = x;
            this.actor.y = y;
            this.actor.z = z;
        }
    }

    public void onNotify(String message) {
        messages.add(message);
    }

}
