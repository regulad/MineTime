package xyz.regulad.minetime.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import xyz.regulad.minetime.MineTimeBukkit;

public class MineTimeOffCommand implements CommandExecutor {
    private final @NotNull MineTimeBukkit plugin;

    public MineTimeOffCommand(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.scheduleEnabled = false;
        sender.sendMessage(Component.text("Disabled MineTime!").color(NamedTextColor.YELLOW));
        return true;
    }
}
