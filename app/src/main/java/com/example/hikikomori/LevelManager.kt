package com.example.hikikomori

class LevelManager {
    var level = 0

    fun timeToLevel(time: Int = 0): Int {
        level = time / 10
        return level
    }

    fun getImageOfTree(): Int {
        val drawableResource = when (level) {
            1 -> R.drawable.tree_1
            2 -> R.drawable.tree_2
            else -> R.drawable.tree_3
        }

        return drawableResource
    }
}