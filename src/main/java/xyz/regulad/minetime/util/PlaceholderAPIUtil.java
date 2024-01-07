package xyz.regulad.minetime.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIUtil {
    private PlaceholderAPIUtil() {}

    public static boolean isPlaceholderAPIInstalled() {
        return Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static @NotNull String substitutePlaceholders(final @NotNull String string, final @NotNull Player player) {
        if (isPlaceholderAPIInstalled()) {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, string);
        } else {
            return string;
        }
    }
}
