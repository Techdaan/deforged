/*
 * Deforged - A free-and-open-source project to enhance Pixelmon Reforged
 * Copyright (C) 2023 - Daniël Voort
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.pokecentral.deforged.utils;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.Resources;
import net.minecraft.util.ResourceLocation;

public class OverlayUtils {
	public static ResourceLocation getOverlaySprite(Pokemon pokemon, boolean isSentOut, boolean selected) {
		if (selected) {
			if (pokemon.isFainted()) {
				return Resources.faintedSelected;
			} else if (isSentOut) {
				return Resources.releasedSelected;
			} else {
				return Resources.selected;
			}
		} else if (pokemon.isFainted()) {
			return Resources.fainted;
		} else if (isSentOut) {
			return Resources.released;
		} else {
			return Resources.normal;
		}
	}
}
