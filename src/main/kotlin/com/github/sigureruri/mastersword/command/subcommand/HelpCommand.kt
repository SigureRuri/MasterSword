package com.github.sigureruri.mastersword.command.subcommand

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class HelpCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val message = arrayOf(
                "${ChatColor.DARK_AQUA}[${ChatColor.AQUA}---------- ${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Master Sword ${ChatColor.AQUA}----------${ChatColor.DARK_AQUA}]",
                "",
                "${ChatColor.DARK_AQUA}/$label help ${ChatColor.LIGHT_PURPLE}- ${ChatColor.AQUA}show this help",
                "${ChatColor.DARK_AQUA}/$label give mastersword ${ChatColor.LIGHT_PURPLE}- ${ChatColor.AQUA}Get item",
                "",
                "${ChatColor.DARK_AQUA}[${ChatColor.AQUA}--------------------------------${ChatColor.DARK_AQUA}]"
        )
        sender.sendMessage(message)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf()
    }
}