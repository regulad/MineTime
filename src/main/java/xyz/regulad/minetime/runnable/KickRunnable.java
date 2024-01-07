package xyz.regulad.minetime.runnable;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.regulad.minetime.MineTimeBukkit;
import xyz.regulad.minetime.util.MinetimePermissions;

public class KickRunnable implements Runnable {
    private final @NotNull MineTimeBukkit plugin;

    public KickRunnable(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (plugin.loginPermittedNow()) {
            return; // The server is open right now. Don't kick anyone.
        }

        // We need to kick everyone who isn't allowed to be on the server right now.
        for (final @NotNull Player player : plugin.getServer().getOnlinePlayers()) {
            if (!player.hasPermission(MinetimePermissions.BYPASS_PERMISSION)) {
                player.kick(plugin.config.getKickMessage(player), PlayerKickEvent.Cause.WHITELIST);
            }
        }
    }
}
