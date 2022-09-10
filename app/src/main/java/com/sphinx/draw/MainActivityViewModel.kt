package com.sphinx.draw

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel


class Line(val path: Path, val color: Color, val width:Float)

private class Brush;


enum class Menu{
    COLORS,
    WIDTHS
}

class MainActivityViewModel:ViewModel() {
    val colors = listOf(
        Color.Black,
        Color.DarkGray,
        Color.Gray,
        Color.LightGray,
        Color.White,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Cyan,
        Color.Magenta
    )
    val widths = listOf<Float>(10f,20f,40f,80f);


    var color by mutableStateOf(colors[0])
        private set

    var width by mutableStateOf(widths[0])
        private set

    val lines = mutableStateListOf<Line>()


    val current = mutableStateListOf<Offset>();


    var menu by mutableStateOf(Menu.COLORS)
        private set

    val path
        get() = current.toPath()


    fun onDraw(offset: Offset){
        current.add(offset)
    }

    fun onStopDrawing(){
        lines.add(Line(current.toPath(), color, width))
        current.clear()

        current.toPath().toString();
    }

    fun undo() = lines.removeLastOrNull();

    fun clear() = lines.clear();

    fun changeColor(color: Color){
        this.color = color
    }

    fun changeMenu(menu: Menu){
        this.menu = menu
    }

    fun changeWidth(width: Float) {
        this.width = width
    }


}