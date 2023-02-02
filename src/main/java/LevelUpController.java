public class LevelUpController {
    private static LevelUpOption[] options = new LevelUpOption[] {
            new LevelUpOption("Increased hit points") {
                public void invoke(Actor actor) {
                    actor.modifyMaxHp(25);
                    actor.setHp(actor.getMaxHp());
                    actor.setFatigue(actor.getFatigue()+100);
                    actor.doAction("look a lot healthier");
                }
            },
             new LevelUpOption("Increased attack value") {
                public void invoke(Actor actor) {
                    actor.modifyAttack(3);
                    actor.modifyMaxHp(10);
                    actor.setHp(actor.getMaxHp());
                    actor.setFatigue(actor.getFatigue()+100);
                    actor.doAction("look stronger");
                }
            }, new LevelUpOption("Increased defense value") {
                    public void invoke(Actor actor) {
                        actor.modifyDefense(3);
                        actor.modifyMaxHp(10);
                        actor.setHp(actor.getMaxHp());
                        actor.setFatigue(actor.getFatigue()+100);
                        actor.doAction("look a little tougher");
                    }
            }
    };

public void autoLevelUp(Actor actor) {
        options[(int) (Math.random() * options.length)].invoke(actor);
    }

}
