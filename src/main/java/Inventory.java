public class Inventory {
    private Item[] items;
    int maxInvSlots;


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
        for (Item item : this.items) {
            if (item != null) {
                ++invSize;
            }
        }
        return invSize == this.items.length;
    }

    public boolean contains(Item item) {
        Item[] var5;
        int var4 = (var5 = this.items).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Item i = var5[var3];
            if (i == item) {
                return true;
            }
        }

        return false;
    }

}
