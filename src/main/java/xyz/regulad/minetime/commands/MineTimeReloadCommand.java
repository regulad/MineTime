package xyz.regulad.minetime.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.regulad.minetime.MineTimeBukkit;

public class MineTimeReloadCommand implements CommandExecutor {
    private final @NotNull MineTimeBukkit plugin;

    public MineTimeReloadCommand(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.config.reloadConfig();
        sender.sendMessage(Component.text("Reloaded config!").color(NamedTextColor.YELLOW));
        return true;
    }
}
