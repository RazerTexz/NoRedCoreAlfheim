package dev.redstudio.alfheim.mixin;

import dev.redstudio.alfheim.api.ILightInfoProvider;
import dev.redstudio.alfheim.api.ILitBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/// @author Luna Mira Lage (Desoroxxx)
/// @version 2023-09-06
/// @since 1.0
@Mixin(BlockStateContainer.class)
public abstract class BlockStateContainerMixin implements ILightInfoProvider {

    @Shadow @Final private Block block;

    @Override
    public final int alfheim$getLightFor(final IBlockAccess iBlockAccess, final EnumSkyBlock lightType, final BlockPos blockPos) {
        return ((ILitBlock) block).alfheim$getLightFor(((IBlockState) this), iBlockAccess, lightType, blockPos);
    }

    @Override
    public final boolean alfheim$useNeighborBrightness(final EnumFacing facing, final IBlockAccess blockAccess, final BlockPos blockPos) {
        return ((ILitBlock) block).alfheim$useNeighborBrightness(((IBlockState) this), facing, blockAccess, blockPos);
    }

    @Override
    public final int alfheim$getLightOpacity(final EnumFacing facing, final IBlockAccess blockAccess, final BlockPos blockPos) {
        return ((ILitBlock) block).alfheim$getLightOpacity(((IBlockState) this), facing, blockAccess, blockPos);
    }

    @Mixin(BlockStateContainer.StateImplementation.class)
    public static class StateImplementationMixin implements ILightInfoProvider {

        @Shadow @Final private Block block;

        @Override
        public final int alfheim$getLightFor(final IBlockAccess iBlockAccess, final EnumSkyBlock lightType, final BlockPos blockPos) {
            return ((ILitBlock) block).alfheim$getLightFor(((IBlockState) this), iBlockAccess, lightType, blockPos);
        }

        @Override
        public final boolean alfheim$useNeighborBrightness(final EnumFacing facing, final IBlockAccess blockAccess, final BlockPos blockPos) {
            return ((ILitBlock) block).alfheim$useNeighborBrightness(((IBlockState) this), facing, blockAccess, blockPos);
        }

        @Override
        public final int alfheim$getLightOpacity(final EnumFacing facing, final IBlockAccess blockAccess, final BlockPos blockPos) {
            return ((ILitBlock) block).alfheim$getLightOpacity(((IBlockState) this), facing, blockAccess, blockPos);
        }
    }
}
