# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Data Models & Sealed classes
-keep class com.example.fontsizecontroller.model.** { *; }

# Keep Receivers & Notification Services
-keep class com.example.fontsizecontroller.service.** { *; }

# Keep Android Components referenced in AndroidManifest
-keep public class com.example.fontsizecontroller.MainActivity extends androidx.activity.ComponentActivity
-keep public class com.example.fontsizecontroller.service.FontSizeNotificationReceiver extends android.content.BroadcastReceiver

# Keep Jetpack Compose runtime
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.material3.** { *; }

# Preserve line numbers for stack traces
-keepattributes SourceFile,LineNumberTable