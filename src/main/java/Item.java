public class Item {
    private char character;
    private String name;
    private String description;
    private int attackValue;
    private int defenseValue;

    public Item(char character, String name, String description, int attackValue, int defenseValue) {
        this.character = character;
        this.name = name;
        this.description = description == null? name : description;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    public String getItemStats() {
        String itemStats = "";
        if(this.attackValue != 0){
            itemStats = itemStats + " " + this.attackValue;
        }
        if(this.defenseValue != 0){
            itemStats = itemStats + " " + this.defenseValue;
        }
        return itemStats;
    }
}
