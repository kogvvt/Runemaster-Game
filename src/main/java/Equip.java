public class Equip extends InventoryScreen{
    public Equip(Actor actor){
        super(actor);
    }

    protected String getDisplayName(){
        return "equip";
    }

    public boolean isAcceptable(Item item){
        return item.getAttackValue()>0 || item.getDefenseValue()>0;
    }

    protected Screen use(Item item){
        this.actor.equip(item);
        return null;
    }
}
