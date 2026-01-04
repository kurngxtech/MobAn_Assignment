package com.example.d1_jetpackcompose.ui.screens.compactPhone.welcomeAuthScreens // Assuming this is your package


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.d1_jetpackcompose.R
import com.example.d1_jetpackcompose.ui.components.RoundedButton
import com.example.d1_jetpackcompose.ui.navigation.AppRoutes
import com.example.d1_jetpackcompose.ui.theme.SmartFitTypography
import com.example.d1_jetpackcompose.ui.theme.SmartFitTheme

@Composable
fun WelcomePage(modifier: Modifier = Modifier, navController : NavController) {
    val backgroundImagePainter = painterResource(R.drawable.background_login)

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = backgroundImagePainter,
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column ( // column untuk wrapper logo,text,button
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .padding(top = 20.dp)
        ) {
            // Smartfit logo
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.smartfit_logo),
                    contentDescription = "SmartFit Logo",
                    modifier = Modifier
                        .size(180.dp)
                )
            }

            // empty spacer
            Spacer(
                modifier = Modifier
                    .fillMaxHeight(0.65f)
            )

            // column for text and button below
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.58f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Text judul dan deskripsi singkat (atas)
                Text(
                    text = "Track your steps",
                    style = SmartFitTypography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(1.dp))

                Text(
                    text = "and get healthier life",
                    style = SmartFitTypography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(1.dp))

                Text(
                    text = "check every steps and every distance you take",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(1.dp))
                Text(
                    text = "and get healthier life",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White
                )
            }

            // button
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                RoundedButton(
                    onClick = {
                        navController.navigate(AppRoutes.LOGIN) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Start your journey right now",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.size(5.dp))

                Text(
                    text = "!",
                    style = SmartFitTypography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun BasicLayoutPreview() {
    SmartFitTheme {
        WelcomePage(navController = rememberNavController())
    }
}