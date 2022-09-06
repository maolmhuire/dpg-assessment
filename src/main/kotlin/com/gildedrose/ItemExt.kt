package com.gildedrose

import com.gildedrose.ItemConstants.AGED_BRIE
import com.gildedrose.ItemConstants.BACKSTAGE_PASSES
import com.gildedrose.ItemConstants.CONJURED
import com.gildedrose.ItemConstants.MAX_QUALITY
import com.gildedrose.ItemConstants.MIN_QUALITY
import com.gildedrose.ItemConstants.SULFURAS
import com.gildedrose.ItemConstants.TICKET_SELL_IN_LESS_THAN_ELEVEN
import com.gildedrose.ItemConstants.TICKET_SELL_IN_LESS_THAN_SIX

fun Item.updateItem() {

    if (isItemImmutable()) return

    when {
        isItemValueLostAfterSellBy() && isAfterSellBy() -> {
            quality = 0
        }
        doesItemValueIncreaseOverTime() -> {
            incrementQuality()
            if (name.startsWith(BACKSTAGE_PASSES, true)) {
                incrementForConcertTickets()
            }
            if (name.startsWith(AGED_BRIE, true) && isAfterSellBy()) {
                incrementQuality()
            }
        }
        else -> {
            val decrementValue = if (doesItemValueDegradeDoubly()) 2 else 1
            decrementQuality(decrementValue)
            if (isAfterSellBy()) {
                decrementQuality(decrementValue)
            }
        }
    }

    sellIn--
}

private fun Item.doesItemValueIncreaseOverTime(): Boolean =
    (name.startsWith(AGED_BRIE, true)
            || name.startsWith(BACKSTAGE_PASSES, true))

private fun Item.isItemImmutable(): Boolean =
    name.startsWith(SULFURAS, true)

private fun Item.isItemValueLostAfterSellBy(): Boolean =
    name.startsWith(BACKSTAGE_PASSES, true)

private fun Item.doesItemValueDegradeDoubly(): Boolean =
    name.startsWith(CONJURED, true)

private fun Item.isAfterSellBy(): Boolean = sellIn < 0

private fun Item.incrementQuality(incrementValue: Int = 1) =
    if ((quality + incrementValue) <= MAX_QUALITY) {
        quality += incrementValue
    } else {
        quality = MAX_QUALITY
    }

private fun Item.decrementQuality(decrementValue: Int = 1) =
    if ((quality - decrementValue) >= MIN_QUALITY) {
        quality -= decrementValue
    } else {
        quality = MIN_QUALITY
    }

private fun Item.incrementForConcertTickets() {
    if (sellIn < TICKET_SELL_IN_LESS_THAN_ELEVEN) {
        incrementQuality()
        if (sellIn < TICKET_SELL_IN_LESS_THAN_SIX) {
            incrementQuality()
        }
    }
}