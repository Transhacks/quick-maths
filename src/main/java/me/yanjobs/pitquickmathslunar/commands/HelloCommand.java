package me.yanjobs.pitquickmathslunar.commands;

import club.maxstats.weave.loader.api.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class HelloCommand extends Command {
    public HelloCommand() {
        super("hello", "hi", "test");
    }
    public void handle(String[] args) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Hi :)"));
    }
}
