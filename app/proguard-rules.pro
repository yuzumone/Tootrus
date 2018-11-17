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
## Android architecture components: Lifecycle
# LifecycleObserver's empty constructor is considered to be unused by proguard
# Android Archtecture Component
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}
# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
-keep class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
-keepclassmembers class android.arch.** { *; }
-keep class android.arch.** { *; }
-dontwarn android.arch.**
# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
# fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep,allowobfuscation @interface com.facebook.soloader.DoNotOptimize
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
# Do not strip any method/class that is annotated with @DoNotOptimize
-keep @com.facebook.soloader.DoNotOptimize class *
-keepclassmembers class * {
    @com.facebook.soloader.DoNotOptimize *;
}
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
# Jsoup
-keeppackagenames org.jsoup.nodes