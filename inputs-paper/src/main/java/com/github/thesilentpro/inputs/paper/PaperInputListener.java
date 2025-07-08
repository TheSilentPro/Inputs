package com.github.thesilentpro.inputs.paper;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperInputListener implements Listener {

    private final PaperInputRegistry registry;

    public PaperInputListener(PaperInputRegistry registry) {
        this.registry = registry;
    }

    public PaperInputListener() {
        this.registry = PaperInputRegistry.INSTANCE;
    }

    public PaperInputListener register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        return this;
    }

    // handlers might want to cancel the event, therefor MONITOR is not used.
    // HIGHEST to allow other handlers to cancel the event
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInput(AsyncChatEvent event) {
        registry.process(event.getPlayer().getUniqueId(), event.message(), event);
    }

}