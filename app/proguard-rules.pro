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
# mastodon4j
-keep class com.sys1yagi.mastodon4j.api.entity.Application
# gson
-keepclassmembers enum * { *; }
