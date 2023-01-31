import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorAi {
    protected Actor actor;

    private Map<String, String> items;

    public ActorAi(Actor actor) {
        this.actor = actor;
        this.actor.setActorAi(this);
        this.items = new HashMap();
    }

    public String getItemName(Item item){
        String name = (String) this.items.get(item.getName());
        return name != null ? item.getDescription(): name;
    }

    public void setName(Item item, String name){
        this.items.put(item.getName(), name);
    }

    public void onEnter(int x, int y, int z, Tile tile){
        if(tile.isGround()){
            this.actor.x = x;
            this.actor.y = y;
            this.actor.z = z;
        }else{
            this.actor.doAction("bumped!");
        }
    }
    public void onUpdate(){

    }
    public void onNotify(String message){

    }
    public boolean canSee(int wx, int wy, int wz) {
        if (actor.z != wz)
            return false;

        if ((actor.x-wx)*(actor.x-wx) + (actor.y-wy)*(actor.y-wy) > actor.getVision()*actor.getVision())
            return false;

        for (Point p : new Line(actor.x, actor.y, wx, wy)){
            if (actor.realTile(p.x, p.y, wz).isGround() || p.x == wx && p.y == wy)
                continue;

            return false;
        }

        return true;
    }

    public void wander() {
        int mx = (int)(Math.random() * 3.0) - 1;
        int my = (int)(Math.random() * 3.0) - 1;
        Actor other = this.actor.actor(this.actor.x + mx, this.actor.y + my, this.actor.z);
        if ((other != null && other.getName().equals(this.actor.getName())) || !this.actor.tile(this.actor.x + mx, this.actor.y + my, this.actor.z).isGround()) {
            this.actor.moveBy(mx, my, 0);
        }
    }

    public void onGainLevel() {
        (new LevelUpController()).autoLevelUp(this.actor);
    }

    public Tile rememberedTile(int wx, int wy, int wz) {
        return Tile.UNKNOWN;
    }

    protected boolean canPickup() {
        return this.actor.item(this.actor.x, this.actor.y, this.actor.z) != null && !this.actor.getInventory().isFull();
    }

    public void hunt(Actor target) {
        List<Point> points = (new Path(this.actor, target.x, target.y)).points();
        int mx = ((Point)points.get(0)).x - this.actor.x;
        int my = ((Point)points.get(0)).y - this.actor.y;
        this.actor.moveBy(mx, my, 0);
    }

}
