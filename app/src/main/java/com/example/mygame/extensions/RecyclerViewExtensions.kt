package com.example.mygame.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView?.getCurrentPosition() : Int {
    (this?.layoutManager as LinearLayoutManager).run {
        val first = this.findFirstVisibleItemPosition()
        val first_c = this.findFirstCompletelyVisibleItemPosition()

        if (first == first_c)
            return first + 1
        else
            return first + 2
    }
}