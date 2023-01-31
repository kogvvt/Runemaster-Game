public class Consume extends InventoryScreen{
    public Consume(Actor player) {
        super(player);
    }

    @Override
    protected String getDisplayName() {
        return "nom nom nom";
    }

    @Override
    public boolean isAcceptable(Item item) {
        return item.getFatigue() != 0;
    }

    @Override
    protected Screen use(Item item) {
        actor.eat(item);
        return null;
    }
}
