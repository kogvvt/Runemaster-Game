public class DraugrAi extends ActorAi {
    private Actor player;

    public DraugrAi(Actor actor, Actor player) {
        super(actor);
        this.player = player;
    }

    public void onUpdate(){
        if (Math.random() < 0.2) {
            return;
        }

        if (actor.canSee(player.x, player.y, player.z)) {
            hunt(player);
        }else {
            wander();
        }
    }
}
