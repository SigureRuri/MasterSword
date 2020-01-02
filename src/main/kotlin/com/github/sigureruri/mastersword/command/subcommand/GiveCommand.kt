package com.github.sigureruri.mastersword.command.subcommand

import com.github.sigureruri.mastersword.command.yaml.Config
import com.github.sigureruri.mastersword.item.Items
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class GiveCommand : TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val subCommand = if (args.isEmpty()) "" else args[1].toLowerCase()
        when (args.size) {
            2 -> {
                when (subCommand) {
                    "mastersword" -> {
                        if (sender !is Player) {
                            sender.sendMessage("${ChatColor.RED}このコマンドはゲーム内から実行してください")
                            return true
                        }
                        if (Config.getAwakingMasterSwordWorldNames().contains(sender.world.name)) {
                            sender.inventory.addItem(Items.getMasterSword(true))
                        } else {
                            sender.inventory.addItem(Items.getMasterSword(false))
                        }

                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val subCommand = if (args.isEmpty()) "" else args[1].toLowerCase()
        val list = mutableListOf<String>()
        val subCommands = listOf("mastersword")
        when (args.size) {
            2 -> {
                subCommands.forEach {
                    if (it.startsWith(subCommand)) list.add(it)
                }
            }
        }
        return list
    }
}