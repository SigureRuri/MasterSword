package com.github.sigureruri.mastersword.item

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import java.util.*

object Items {
    fun getMasterSword(isAwakening: Boolean, durability: Int = 0): ItemStack {
        return ItemStack(Material.DIAMOND_SWORD).also { itemStack ->
            itemStack.itemMeta = itemStack.itemMeta.also { itemMeta ->
                (itemMeta as Damageable).damage = durability

                if (isAwakening) {
                    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 10, true)
                    itemMeta.addEnchant(Enchantment.DURABILITY, 5, true)
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND))
                    itemMeta.setCustomModelData(19860222)
                    itemMeta.setDisplayName("${ChatColor.AQUA}${ChatColor.BOLD}Master Sword")
                } else {
                    itemMeta.setCustomModelData(19860221)
                    itemMeta.setDisplayName("${ChatColor.DARK_AQUA}${ChatColor.BOLD}Master Sword")
                }
            }
        }
    }

    fun isMasterSword(comparisonSource: ItemStack, isAwakening: Boolean): Boolean {
        val comparisonDestination = getMasterSword(isAwakening)

        if (comparisonSource.equals(comparisonDestination)) return true
        if (comparisonSource == comparisonDestination) return true

        if (comparisonSource.hasItemMeta() && comparisonDestination.hasItemMeta()) {

            val cloneSource = comparisonSource.clone()
            val cloneDestination = comparisonDestination.clone().also { itemStack -> itemStack.itemMeta = itemStack.itemMeta!!.also { itemMeta -> (itemMeta as Damageable).damage = (cloneSource.itemMeta!! as Damageable).damage } }

            if (cloneSource.equals(cloneDestination)) return true
            if (cloneSource == cloneDestination) return true

            val sourceItemMeta = cloneSource.itemMeta!!
            val destinationItemMeta = cloneDestination.itemMeta!!

            if (sourceItemMeta.hasAttributeModifiers() && destinationItemMeta.hasAttributeModifiers()) {
                val sourceAttributeModifiers = sourceItemMeta.attributeModifiers!!
                val destinationAttributeModifier = destinationItemMeta.attributeModifiers!!

                if (sourceAttributeModifiers.size() == destinationAttributeModifier.size()) {
                    var isEquals = false

                    sourceAttributeModifiers.entries().forEach sForEach@ { (sKey, sValue) ->
                        destinationAttributeModifier.entries().forEach dForEach@ { (dKey, dValue) ->
                            if (sKey == dKey && attributeEqualsOtherThanUUID(sValue, dValue)) {
                                isEquals = true
                                return@sForEach
                            }
                        }
                        if (!isEquals) return false
                    }

                    sourceItemMeta.attributeModifiers = null
                    destinationItemMeta.attributeModifiers = null
                    cloneSource.itemMeta = sourceItemMeta
                    cloneDestination.itemMeta = destinationItemMeta

                    if (cloneSource.equals(cloneDestination)) return true
                }
            }
        }


        return false
    }

    private fun attributeEqualsOtherThanUUID(attribute: AttributeModifier, other: AttributeModifier): Boolean {
        val slots = if (attribute.slot != null) attribute.slot == other.slot else other.slot == null
        return attribute.name == other.name && attribute.amount == other.amount && attribute.operation == other.operation && slots
    }
}