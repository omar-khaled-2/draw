package com.sphinx.draw

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sphinx.draw.ui.theme.DrawTheme
import kotlinx.coroutines.launch


fun List<Offset>.toPath():Path{
    val path = Path();
    if(this.isNotEmpty()) path.moveTo(this[0].x,this[0].y)
    for(i in 1 until this.size){
        path.lineTo(this[i].x,this[i].y)
    }
    return path

}







class MainActivity : ComponentActivity() {


    private val viewModel:MainActivityViewModel by viewModels()


    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            DrawTheme {
                // A surface container using the 'background' color from the theme







                val scaffoldState = rememberBottomSheetScaffoldState()

                val scope = rememberCoroutineScope()



                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    BottomSheetScaffold(
                        sheetPeekHeight = 50.dp,
                        scaffoldState = scaffoldState,
                        sheetContent = {


                                Column() {
                                    Box(
                                        Modifier
                                            .height(50.dp)
                                            .fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        IconButton(onClick = {
                                            scope.launch {
                                                if(scaffoldState.bottomSheetState.isCollapsed) scaffoldState.bottomSheetState.expand()
                                            }
                                        }) {
                                            Icon(imageVector = Icons.Filled.ExpandLess, contentDescription = "tools" )

                                        }
                                    }

                                    Slider(value = viewModel.width, onValueChange = {
                                        viewModel.changeWidth(it)
                                    }, valueRange = 10f..100f, steps = 20, modifier = Modifier.height(60.dp))
                                    LazyRow(
                                            contentPadding = PaddingValues(20.dp),
                                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                                        ) {
                                            items(viewModel.colors) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .background(it)
                                                        .size(40.dp)
                                                        .clickable { viewModel.changeColor(it) }
                                                )
                                            }
                                        }








                                    Row(
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        IconButton(onClick = {viewModel.undo()}) {
                                            Icon(imageVector = Icons.Filled.Undo, contentDescription = null)
                                        }
                                        IconButton(onClick = {viewModel.clear()}) {
                                            Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(viewModel.color)
                                                .size(40.dp)
                                                .clickable { viewModel.changeMenu(Menu.COLORS) }
                                        )

                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .size(40.dp)
                                                .clickable { viewModel.changeMenu(Menu.WIDTHS) }

                                            ,
                                            contentAlignment = Alignment.Center
                                        ){
                                            Box(
                                                modifier = Modifier
                                                    .size(viewModel.width.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        Color.Black
                                                    )
                                            )
                                        }
                                    }
                                }
                            }




                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            ) {
                            Canvas(modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .pointerInteropFilter {
                                    when (it.action) {
                                        MotionEvent.ACTION_DOWN -> viewModel.onDraw(
                                            Offset(
                                                it.x,
                                                it.y
                                            )
                                        )
                                        MotionEvent.ACTION_MOVE -> viewModel.onDraw(
                                            Offset(
                                                it.x,
                                                it.y
                                            )
                                        )
                                        MotionEvent.ACTION_UP -> viewModel.onStopDrawing()
                                    }
                                    true
                                }
                            ){
                                for(line in viewModel.lines){
                                    drawPath(
                                        path = line.path,
                                        color = line.color,
                                        style = Stroke(line.width,cap = StrokeCap.Round,join = StrokeJoin.Round)
                                    )
                                }
                                drawPath(
                                    path = viewModel.path,
                                    color = viewModel.color,
                                    alpha = 1f,
                                    style = Stroke(viewModel.width, cap = StrokeCap.Round, join = StrokeJoin.Round),

                                )

                            }

                            }




                        }

                    }
                }
                    }

        }
}
