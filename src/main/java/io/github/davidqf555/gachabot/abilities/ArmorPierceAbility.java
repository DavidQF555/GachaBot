package io.github.davidqf555.gachabot.abilities;

public class ArmorPierceAbility extends AbilityAbstract {

    @Override
    public double getOpponentDefenseFactor() {
        return 0.5;
    }

    @Override
    public String getDescription() {
        return "The opponent's defense is half as effective";
    }
}
