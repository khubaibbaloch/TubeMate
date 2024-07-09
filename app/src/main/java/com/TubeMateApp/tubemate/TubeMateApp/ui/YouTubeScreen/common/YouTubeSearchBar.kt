package com.TubeMateApp.tubemate.TubeMateApp.ui.YouTubeScreen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun YouTubeSearchBar(
    searchValue: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        BasicTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            keyboardActions = KeyboardActions(

            ),
            cursorBrush = SolidColor(Black),
            value = searchValue,
            singleLine = true,
            textStyle = TextStyle(
                color = Black,
                fontSize = 14.sp
            ),
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->

                Box {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(White)
                            .border(.5.dp, Black, RoundedCornerShape(8.dp))
                            .padding(start = 8.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) { innerTextField() }
                    Text(
                        text = if (searchValue.isEmpty()) placeholderText else "",
                        color = Black,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(Alignment.CenterStart)
                            .alpha(.5f)
                    )
                }
            }
        )
    }
}
