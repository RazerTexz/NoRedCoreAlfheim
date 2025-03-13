package dev.redstudio.alfheim.asm;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.Context;
import zone.rong.mixinbooter.IEarlyMixinLoader;
import zone.rong.mixinbooter.IMixinConfigHijacker;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dev.redstudio.alfheim.ProjectConstants.ID;
//import static dev.redstudio.alfheim.ProjectConstants.LOGGER;
import static dev.redstudio.alfheim.ProjectConstants.NAME;

/// @author Luna Mira Lage (Desoroxxx)
/// @version 2025-02-14
/// @since 1.0
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("dev.redstudio." + ID + ".asm")
public final class AlfheimPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader, IMixinConfigHijacker {

    @Override
    public final String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public final String getModContainerClass() {
        return null;
    }

    @Override
    public final String getSetupClass() {
        return null;
    }

    @Override
    public final void injectData(final Map<String, Object> data) {
    }

    @Override
    public final String getAccessTransformerClass() {
        return null;
    }

    @Override
    public final List<String> getMixinConfigs() {
        return ImmutableList.of("mixins." + ID + ".json");
    }

    @Override
    public final boolean shouldMixinConfigQueue(final String mixinConfig) {
        switch (mixinConfig) {
            case "mixins." + ID + ".json":
                return !isCubicChunksInstalled();
            default:
                return true;
        }
    }

    /// Checks if Cubic Chunks is installed.
    ///
    /// @return True if Cubic Chunks is installed, false otherwise.
    public static final boolean isCubicChunksInstalled() {
        try {
            Class.forName("io.github.opencubicchunks.cubicchunks.core.asm.CubicChunksCoreContainer");
        } catch (final ClassNotFoundException ignored) {
            return false;
        }

        //LOGGER.warn("Cubic Chunks was detected, it uses it's own lighting engine, {} will not load", NAME);
        return true;
    }

    @Override
    public final Set<String> getHijackedMixinConfigs() {
        return Collections.singleton("mixins.phosphor.json");
    }

    @Override
    public final Set<String> getHijackedMixinConfigs(final Context context) {
        if (!context.isModPresent("phosphor-lighting"))
            return Collections.emptySet();

        //LOGGER.warn("Phosphor/Hesperus was detected, {} replaces them entirely, will now attempt hijacking, you should remove Phosphor/Hesperus", NAME);
        return getHijackedMixinConfigs();
    }
}
