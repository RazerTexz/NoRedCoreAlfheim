package dev.redstudio.alfheim.utils;

/// A utility class that offers efficient clamping methods.
///
/// These functions are optimized for speed and provided for every primitive datatype.
///
/// @author Luna Mira Lage (Desoroxxx)
/// @version 2025-02-01
/// @since 0.5
@SuppressWarnings("ManualMinMaxCalculation")
public final class ClampUtil {
    private ClampUtil() { }

    /// Clamps a value within a specified range `min, max`, checking for the minimum value first.
    ///
    /// If the input is less than min, it returns min. If the input is greater than max, it returns max.
    /// Otherwise, it returns the input.
    ///
    /// @param input The input value to clamp
    /// @param min The minimum value to clamp to
    /// @param max The maximum value to clamp to
    ///
    /// @return The clamped value
    public static final int clampMinFirst(final int input, final int min, final int max) {
        return input < min ? min : input > max ? max : input;
    }
}
