package com.github.sigureruri.mastersword.command.yaml

import com.github.sigureruri.mastersword.MasterSword

object Config : Yaml(MasterSword.PLUGIN, "config.yml", true) {
    fun getAwakingMasterSwordWorldNames(): List<String> {
        return getStringList("AwakingMasterSwordWorld")
    }
}