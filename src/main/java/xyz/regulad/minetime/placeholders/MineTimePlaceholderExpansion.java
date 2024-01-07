package xyz.regulad.minetime.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.minetime.MineTimeBukkit;

public class MineTimePlaceholderExpansion extends PlaceholderExpansion {
    private final @NotNull MineTimeBukkit plugin;

    public MineTimePlaceholderExpansion(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "minetime";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public @Nullable String onRequest(final @Nullable OfflinePlayer player, final @NotNull String identifier) {
        return null; // TODO
    }
}
