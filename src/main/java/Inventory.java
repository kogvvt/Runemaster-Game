public class Inventory {
    private Item[] items;

    public Item[] getItems() {
        return items;
    }
    public Item getItem(int index) {
        return this.items[index];
    }

    public Inventory(int maxInvSlots) {
        this.items = new Item[maxInvSlots];
    }

    public void add(Item item) {
        for(int i = 0 ; i < this.items.length; i++) {
            if(this.items[i] == null){
                this.items[i] = item;
                break;
            }
        }
    }

    public void remove(Item item) {
        for(int i = 0 ; i<this.items.length; i++) {
            if(this.items[i] == item){
                this.items[i] = null;
                return;
            }
        }
    }

    public boolean isFull() {
        int invSize = 0;
        for(int i = 0 ; i < this.items.length; i++){
            if(this.items[i] != null){
                ++invSize;
            }
        }
        if(invSize == this.items.length){
            return true;
        }else{
            return false;
        }
    }

}
