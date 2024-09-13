# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep rules for Instaloader
-keep class org.json.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Coil (image loading library)
-keep class coil.** { *; }
-keep class coil.request.** { *; }
-keepclassmembers class coil.** { *; }
-keepclassmembers class ** {
    @coil.annotation.Experimental* <methods>;
}

# Kotlin Coroutines
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }

# Retrofit and OkHttp
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep class com.squareup.okio.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn okio.**

# Suppress warnings for Conscrypt
-dontwarn org.conscrypt.**

# Suppress warnings for OpenJSSE
-dontwarn org.openjsse.**

# Suppress warnings for internal Java classes
-dontwarn com.android.org.conscrypt.**
-dontwarn org.apache.harmony.xnet.provider.jsse.**
-dontwarn sun.misc.**
-dontwarn sun.net.**
-dontwarn sun.security.**
-dontwarn sun.util.**

# Keep FFmpeg library code
# Only keep FFmpeg executeAsync methods to shrink the library size
-keep class com.arthenica.mobileffmpeg.FFmpeg.** { *; }


