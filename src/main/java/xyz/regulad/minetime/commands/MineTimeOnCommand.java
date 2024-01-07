package xyz.regulad.minetime.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.regulad.minetime.MineTimeBukkit;

public class MineTimeOnCommand implements CommandExecutor {
    private final @NotNull MineTimeBukkit plugin;

    public MineTimeOnCommand(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.scheduleEnabled = true;
        sender.sendMessage(Component.text("Enabled MineTime!").color(NamedTextColor.YELLOW));
        return true;
    }
}
