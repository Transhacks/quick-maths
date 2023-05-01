package me.yanjobs.pitquickmathslunar;

import club.maxstats.weave.loader.api.ModInitializer;
import club.maxstats.weave.loader.api.command.CommandBus;
import club.maxstats.weave.loader.api.event.EventBus;
import me.yanjobs.pitquickmathslunar.commands.HelloCommand;
import me.yanjobs.pitquickmathslunar.events.ChatEvent;

public class PitQuickMathsLunar implements ModInitializer {
    @Override
    public void init() {
        System.out.println("Starting hook... | PitQuickMaths");
        EventBus.subscribe(new ChatEvent());
        CommandBus.register(new HelloCommand());
    }
}
