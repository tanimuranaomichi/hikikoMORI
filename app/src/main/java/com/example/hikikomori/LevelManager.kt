package com.example.hikikomori

class LevelManager {
    private var level = 0

    fun timeToLevel(time: Int = 0): Int {
        level = time / 10// デモ用.本番はtime /100とする
        return level
    }

    fun getImageOfTree(): Int {
        val drawableResource = when (level) {
            0 -> R.drawable.tree_0
            1 -> R.drawable.tree_1
            2 -> R.drawable.tree_2
            3 -> R.drawable.tree_3
            4 -> R.drawable.tree_4
            5 -> R.drawable.tree_5
            6 -> R.drawable.tree_6
            7 -> R.drawable.tree_7
            8 -> R.drawable.tree_8
            9 -> R.drawable.tree_9
            10 -> R.drawable.tree_10
            else -> R.drawable.tree_max
        }

        return drawableResource
    }
}