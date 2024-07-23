package com.TubeMateApp.tubemate.TubeMateApp.ui.PrivacyPolicyScreen

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.TubeMateApp.tubemate.TubeMateApp.ui.InstagramScreen.common.InstagramScreenTopBar
import com.TubeMateApp.tubemate.TubeMateApp.ui.PrivacyPolicyScreen.common.PrivacyPolicyScreenTopBar


@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            PrivacyPolicyScreenTopBar(
                title = "Privacy Policy",
                onIconClick = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState
                        == Lifecycle.State.RESUMED
                    ) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Privacy Policy",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "We value your privacy and are committed to protecting your personal data. This privacy policy will inform you about how we use the data we collect from your interactions with our app.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "1. Data Collection",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "We collect data from the URLs you provide for Instagram, WhatsApp, Facebook, and YouTube. This includes media content such as videos, images, and other related information. We use libraries to fetch this data securely and efficiently.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "2. Data Usage",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "The collected data is used solely for the purpose of providing you with the requested downloads. We do not store, share, or misuse your personal data in any way. The data is processed on your device and is not transmitted to our servers.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "3. Libraries and Tools",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "We utilize libraries such as instaloader and FFmpeg to fetch and process media content. These libraries help us ensure the reliability and efficiency of our services. Additionally, we use the DownloadManager for managing downloads and Jetpack Compose for UI components.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "4. No Harm to User Data",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "We take necessary measures to ensure that our processes do not harm your personal data. Our operations are designed to be safe, secure, and respectful of your privacy. We do not collect or store any personally identifiable information.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "5. Data Retention",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "We do not retain any data on our servers. All data processing is done locally on your device, and any downloaded media is stored based on your device's storage policies. You can delete the downloaded content at any time through your device's file manager.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "6. User Rights",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "As a user, you have the right to know what data is being collected and how it is used. You also have the right to request the deletion of any downloaded content from your device. Our app is designed to give you full control over your data.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "7. Contact Information",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "If you have any questions or concerns about our privacy policy or your data, please feel free to contact us at [Your Contact Email]. We are committed to addressing any issues you may have.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "By using our app, you agree to our privacy policy and the terms outlined herein. We reserve the right to update this policy as needed and will notify users of any significant changes.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
            )
        }
    }
}
