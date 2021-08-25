package com.example.hikikomori

class LevelManager {
    var level = 0

    fun timeToLevel(time: Int = 0): Int {
        level = time / 10// デモ用.本番はtime /100とする
        return level
    }

    fun getTreeImage(): Int {

        return when (level) {
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
    }

    fun getAnimalImage(): Int {
        return if (level > 5) {
            when (level%6) {
                0 -> R.drawable.animal_0
                1 -> R.drawable.animal_1
                2 -> R.drawable.animal_2
                else -> R.drawable.animal_0
            }
        } else {
            R.drawable.animal_0
        }
    }

    fun getBirdImage(): Int {
        return if (level > 5) {
            when (level%5) {
                0 -> R.drawable.bird_0
                1 -> R.drawable.bird_1
                2 -> R.drawable.bird_2
                3 -> R.drawable.bird_3
                else -> R.drawable.bird_0
            }
        } else {
            R.drawable.animal_0
        }
    }
}