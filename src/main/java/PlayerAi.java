import java.util.List;

public class PlayerAi extends ActorAi {
    private List<String> messages;
    private FogOfWar fow;
    public PlayerAi(Actor actor, List<String> messages, FogOfWar fow) {
        super(actor);
        this.messages = messages;
        this.fow = fow;
    }

    @Override
    public void onEnter(int x, int y, int z, Tile tile) {
        if(tile.isGround()){
            this.actor.x = x;
            this.actor.y = y;
            this.actor.z = z;
            Item item = actor.item(actor.x, actor.y, actor.z);
            if (item != null)
                actor.notify("There's a " + actor.nameOf(item) + " here.");

        } else if (tile.isDiggable()) {
            actor.dig(x, y, z);
        }
    }

    public void onNotify(String message) {
        messages.add(message);
    }

    public Tile rememberedTile(int wx, int wy, int wz) {
        return fow.tile(wx, wy, wz);
    }

}
