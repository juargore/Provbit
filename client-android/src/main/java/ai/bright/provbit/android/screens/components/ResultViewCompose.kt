package ai.bright.provbit.android.screens.components

import ai.bright.provbit.demo.entities.ImageAnalysis
import ai.bright.provbit.util.PrePostProcessor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResultViewCompose(
    results: List<ImageAnalysis>?
) {
    if(results == null) return
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Black)
    ) {
        Text(
            text = "Results from the image analysis: ",
            color = Color.White,
            modifier = Modifier.padding(10.dp)
        )
        for(result in results) {
            Text(
                modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                text = String.format(
                    "%s %.2f",
                    PrePostProcessor.mClasses[result.classIndex],
                    result.score
                ),
                color = Color.White,
                maxLines = 4
            )
        }
    }
}
