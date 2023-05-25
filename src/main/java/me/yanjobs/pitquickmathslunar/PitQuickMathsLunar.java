package me.yanjobs.pitquickmathslunar;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;
import net.weavemc.loader.api.event.EventBus;
import me.yanjobs.pitquickmathslunar.commands.HelloCommand;
import me.yanjobs.pitquickmathslunar.events.ChatEvent;

public class PitQuickMathsLunar implements ModInitializer {
    @Override
    public void preInit() {
        System.out.println("Starting hook... | PitQuickMaths");
        EventBus.subscribe(new ChatEvent());
        CommandBus.register(new HelloCommand());
    }
}
