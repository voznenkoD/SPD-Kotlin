package org.xebia.spdmanager.model

data class KitChain(val name: String, val kitRefs: List<Int>) {
    companion object {
        /** kitRefs value meaning "no kit assigned" to a chain slot. */
        const val NO_KIT = -1
    }
}
