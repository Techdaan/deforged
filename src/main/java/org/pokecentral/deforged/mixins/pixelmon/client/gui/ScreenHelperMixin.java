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

package org.pokecentral.deforged.mixins.pixelmon.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHelper.class)
public class ScreenHelperMixin {

	/**
	 * Disables the "vanilla" pixelmon draw
	 */
	@Redirect(method = "drawPokemonHoverInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;draw(Lcom/mojang/blaze3d/matrix/MatrixStack;Ljava/lang/String;FFI)I", ordinal = 0))
	private static int voidInitialDraw(FontRenderer renderer, MatrixStack matrix, String text, float x, float y, int color) {
		return -1;
	}

	/**
	 * Replaces the "vanilla" Pixelmon draw with the formatted display name
	 */
	@Inject(method = "drawPokemonHoverInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;draw(Lcom/mojang/blaze3d/matrix/MatrixStack;Ljava/lang/String;FFI)I", ordinal = 0))
	private static void drawPokemonHoverInfo(MatrixStack matrix, Pokemon pokemon, int x, int y, CallbackInfo ci) {
		Minecraft mc = Minecraft.getInstance();
		if (x - 104 < 0) {
			x = 104;
		}

		ITextComponent name = pokemon.getFormattedDisplayName();
		mc.font.draw(matrix, name, (float) (x - 102), (float) y, 16777215);
	}
}
