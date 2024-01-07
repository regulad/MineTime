package xyz.regulad.minetime.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;
import xyz.regulad.minetime.MineTimeBukkit;
import xyz.regulad.minetime.util.MinetimePermissions;

public class MineTimeListener implements Listener {
    final @NotNull MineTimeBukkit plugin;

    public MineTimeListener(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerLogin(final @NotNull PlayerLoginEvent event) {
        final @NotNull Player player = event.getPlayer();
        if (
                !player.hasPermission(MinetimePermissions.BYPASS_PERMISSION)
                        && !plugin.loginPermittedNow()
        ) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, plugin.config.getKickMessage(player));
        }
    }
}
