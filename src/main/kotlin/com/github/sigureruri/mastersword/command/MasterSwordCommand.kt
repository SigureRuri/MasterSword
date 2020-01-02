package com.github.sigureruri.mastersword.command

import com.github.sigureruri.mastersword.command.subcommand.GiveCommand
import com.github.sigureruri.mastersword.command.subcommand.HelpCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class MasterSwordCommand : TabExecutor {

    companion object {
        val subCommandMap = mapOf<String, TabExecutor>(
                "give" to GiveCommand(),
                "help" to HelpCommand()
        )
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val subCommand = if (args.isEmpty()) "" else args[0]
        subCommandMap.forEach { (key, value) ->
            if (subCommand == key) {
                return value.onCommand(sender, command, label, args)
            }
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val subCommand = if (args.isEmpty()) "" else args[0].toLowerCase()
        val list = mutableListOf<String>()
        if (args.size == 1) {
            subCommandMap.forEach { (key, _) ->
                if (key.startsWith(subCommand)) {
                    list.add(key)
                }
            }
        } else {
            subCommandMap.forEach { (key, value) ->
                if (key.startsWith(subCommand)) {
                    value.onTabComplete(sender, command, alias, args)!!.forEach { list.add(it) }
                }
            }
        }
        return list
    }
}