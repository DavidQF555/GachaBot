package com.david.gachabot.abilities;

@Ability
public class ArmorPierceAbility extends AbilityAbstract {

	private static final long serialVersionUID = 6;

	@Override
	public double getOpponentDefenseFactor() {
		return 0.5;
	}

	@Override
	public String getName() {
		return "Armor Pierce";
	}

	@Override
	public String getDescription() {
		return "The opponent's defense is half as effective";
	}
}