package com.david.gachabot.abilities;

import java.util.List;

public class ArmorPierceAbility extends Ability {

	private static final long serialVersionUID = 6;

	@Override
	public int onAttack(int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
		return (int) (attack[1] / 10.0 + 2.0 * attack[1] / defend[2] + 0.5);
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
