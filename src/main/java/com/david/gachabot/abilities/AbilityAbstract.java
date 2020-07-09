package com.david.gachabot.abilities;

import java.io.Serializable;
import java.util.List;

public abstract class AbilityAbstract implements Serializable {

	private static final long serialVersionUID = 5;

	public int onAttack(int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
		int damage = (int) (attack[1] / 10.0 + 1.0 * attack[1] / defend[2] + 0.5);
		if(damage < 1) {
			damage = 1;
		}
		return damage;
	}

	public int onDefend(int damage, int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
		return damage;
	}

	public abstract String getName();

	public abstract String getDescription();
}
