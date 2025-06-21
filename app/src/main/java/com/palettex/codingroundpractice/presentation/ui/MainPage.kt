package com.palettex.codingroundpractice.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.palettex.codingroundpractice.ui.theme.CodingRoundPracticeTheme

@Composable
fun MainPage(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                Log.d("TEST","Click button!")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Click")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    CodingRoundPracticeTheme {
        MainPage()
    }
}