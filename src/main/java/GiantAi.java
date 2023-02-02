public class GiantAi extends ActorAi{
    private Actor player;
    public GiantAi(Actor actor, Actor player) {
        super(actor);
        this.player = player;
    }
    public void onUpdate() {
        if (Math.random() < 0.2) {
            return;
        }
        if (actor.canSee(this.player.x, this.player.y, this.player.z)) {
            hunt(this.player);
        } else {
            wander();
        }
    }
}
