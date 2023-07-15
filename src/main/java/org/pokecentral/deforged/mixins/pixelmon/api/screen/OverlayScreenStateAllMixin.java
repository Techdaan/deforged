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

package org.pokecentral.deforged.mixins.pixelmon.api.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.gui.PixelmonOverlayScreen;
import com.pixelmonmod.pixelmon.client.gui.Resources;
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper;
import com.pixelmonmod.pixelmon.client.listener.SendoutListener;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.awt.*;

@Mixin(targets = "com.pixelmonmod.pixelmon.api.screens.OverlayScreenState$1")
public class OverlayScreenStateAllMixin {
	/**
	 * @author Pixelmon Reforged Team, modifications by Daniël Voort
	 * @reason Fix Pokemon nicknames not rendering
	 */
	@Overwrite(remap = false)
	public void renderParty(MatrixStack matrix, Minecraft mc, int selectedIndex, Pokemon[] party) {
		int leftText = 30;
		int topSideBar = mc.getWindow().getGuiScaledHeight() / 2 - PixelmonOverlayScreen.sideBarHeight / 2;
		int topOffset = topSideBar + 5;
		FontRenderer fontRenderer = mc.font;

		if (ClientStorageManager.party.inTemporaryMode()) {
			Color color = ClientStorageManager.party.getTempPartyColor();

			ScreenHelper.drawImageQuad(Resources.dock, matrix, 0.0F, (float) topSideBar, 22.0F, 203.0F, 0.0F, 0.0F, 1.0F, 1.0F, (float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getAlpha(), 0.0F);
			ScreenHelper.drawImageQuad(Resources.padlock, matrix, 0.5F, (float) (topSideBar - 9), 10.0F, 10.0F, 0.0F, 0.0F, 1.0F, 1.0F, (float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getAlpha(), 1.0F);
		} else {
			ScreenHelper.drawImageQuad(Resources.dock, matrix, 0.0F, (float) topSideBar, 22.0F, 203.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F, 0.5F, 0.5F, 1.0F, 0.0F);
		}

		int slot = -1;
		for (Pokemon pokemon : party) {
			int offset = 0;
			++slot;
			int yPos = topOffset + (int) ((float) slot * 30.0f) + 9 + offset;
			if (pokemon != null) {
				boolean isSentOut = SendoutListener.isInWorld(pokemon.getUUID(), mc.level);
				ScreenHelper.drawImageQuad(Resources.textbox, matrix, (float) (leftText - 28), (float) (yPos - 10), 123.0F, 34.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
				float[] texturePair = StatusType.getTexturePos(pokemon.getStatus().type);
				float textureX = texturePair[0];
				float textureY = texturePair[1];
				if (textureX != -1.0F && !pokemon.isFainted()) {
					ScreenHelper.bindTexture(Resources.status);
					ScreenHelper.simpleDrawImageQuad(matrix, (float) (leftText + 56), (float) (yPos + 1), 8.0F, 8.0F, textureX / 768.0F, textureY / 768.0F, (textureX + 240.0F) / 768.0F, (textureY + 240.0F) / 768.0F, 0.0F);
				}

				ITextComponent displayName = pokemon.getFormattedDisplayName();
				ScreenHelper.drawString(matrix, fontRenderer, displayName, (float) (leftText - 2), (float) yPos, 16777215, false, true);
				if (pokemon.getGender() == Gender.MALE && !pokemon.isEgg()) {
					ScreenHelper.drawImageQuad(Resources.male, matrix, (float) (ScreenHelper.getStringWidth(displayName, true) + leftText - 1), (float) yPos, 5.0F, 8.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
				} else if (pokemon.getGender() == Gender.FEMALE && !pokemon.isEgg()) {
					ScreenHelper.drawImageQuad(Resources.female, matrix, (float) (ScreenHelper.getStringWidth(displayName, true) + leftText - 1), (float) yPos, 5.0F, 8.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
				}

				ScreenHelper.drawImageQuad(pokemon.getBall().getGUISprite(), matrix, -3.0F, (float) (yPos - 7), 32.0F, 32.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
				ResourceLocation icon;
				if (slot == selectedIndex) {
					if (pokemon.isFainted()) {
						icon = Resources.faintedSelected;
					} else if (isSentOut) {
						icon = Resources.releasedSelected;
					} else {
						icon = Resources.selected;
					}
				} else if (pokemon.isFainted()) {
					icon = Resources.fainted;
				} else if (isSentOut) {
					icon = Resources.released;
				} else {
					icon = Resources.normal;
				}

				ScreenHelper.drawImageQuad(icon, matrix, -3.0F, (float) (yPos - 7), 32.0F, 32.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
				ScreenHelper.drawImageQuad(pokemon.getSprite(), matrix, 1.0F, (float) (yPos - 6), 24.0F, 24.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);

				if (!pokemon.getHeldItem().isEmpty()) {
					ScreenHelper.drawImageQuad(Resources.heldItem, matrix, 18.0F, (float) (yPos + 13), 6.0F, 6.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
				}

				if (!pokemon.isEgg()) {
					String level = I18n.get("gui.screenpokechecker.lvl") + " " + pokemon.getPokemonLevel();
					float xOffset = (float) (leftText - 1);
					int yOffset = yPos + 1;
					ScreenHelper.drawString(matrix, level, xOffset, (float) (yOffset + 9), 16777215, false, true);
					if (pokemon.isFainted()) {
						xOffset = (float) (leftText + 1 + ScreenHelper.getStringWidth(level, true));
						yOffset = yPos + 1;
						ScreenHelper.drawString(matrix, I18n.get("gui.creativeinv.fainted"), xOffset, (float) (yOffset + 9), 16777215, false, true);
					} else {
						String text = I18n.get("nbt.hp", new Object[0]) + " " + pokemon.getHealth() + "/" + pokemon.getMaxHealth();
						xOffset = (float) (leftText + 2 + ScreenHelper.getStringWidth(level, true));
						yOffset = yPos + 1;
						ScreenHelper.drawString(matrix, text, xOffset, (float) (yOffset + 9), 16777215, false, true);
					}
				}
			} else {
				ScreenHelper.drawImageQuad(Resources.available, matrix, 5.0F, (float) (yPos + 1), 16.0F, 16.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F);
			}
		}
	}
}
