package dev.redstudio.alfheim.lighting;

import atomicstryker.dynamiclights.client.DynamicLights;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;

import static dev.redstudio.alfheim.Alfheim.IS_DYNAMIC_LIGHTS_LOADED;
import static dev.redstudio.alfheim.Alfheim.IS_FLUIDLOGGED_API_LOADED;

/// @author Luna Mira Lage (Desoroxxx)
/// @author embeddedt
/// @since 1.0
public final class LightUtil {

	public static int getLightOpacityForPos(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos, final Chunk chunk) {
		if (IS_FLUIDLOGGED_API_LOADED) {
			final int fluidStateOpacity = FluidState.getFromProvider(chunk, blockPos).getState().getLightOpacity(blockAccess, blockPos);
			return Math.max(fluidStateOpacity, blockState.getLightOpacity(blockAccess, blockPos));
		} else {
			return blockState.getLightOpacity(blockAccess, blockPos);
		}
	}

	public static int getLightValueForPos(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos, final Chunk chunk) {
		if (IS_FLUIDLOGGED_API_LOADED) {
			final int fluidStateLight = FluidState.getFromProvider(chunk, blockPos).getState().getLightValue(blockAccess, blockPos);
			return Math.max(fluidStateLight, getLightValueForState(blockState, blockAccess, blockPos));
		} else {
			return getLightValueForState(blockState, blockAccess, blockPos);
		}
	}

	public static int getLightValueForState(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos blockPos) {
		if (IS_DYNAMIC_LIGHTS_LOADED) {
			return DynamicLights.getLightValue(blockState.getBlock(), blockState, blockAccess, blockPos); // Use the Dynamic Lights implementation
		} else {
			return blockState.getLightValue(blockAccess, blockPos); // Use the vanilla implementation
		}
	}
}
