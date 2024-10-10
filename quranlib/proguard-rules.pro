# ----------------------------------------------------------------------------
# 混淆的压缩比例，0-7
-optimizationpasses 5
# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
# 指定混淆是采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# 指定外部模糊字典 proguard-chinese.txt 改为混淆文件名，下同
#-obfuscationdictionary proguard-o0O.txt
# 指定class模糊字典
#-classobfuscationdictionary proguard-o0O.txt
# 指定package模糊字典
#-packageobfuscationdictionary proguard-o0O.txt
-dontoptimize
-dontshrink
-renamesourcefileattribute Proguard
-keepattributes *JavascriptInterface*,*Annotation*,Exceptions,SourceFile,LineNumberTable,EnclosingMethod,Signature
-dontpreverify
-verbose
-dontwarn android.support.**,android.webkit.WebView,com.android.internal.os.*,android.view.ViewTreeObserver,android.view.Window
-dontwarn com.whsdk.R*
#-dontwarn com.ipaynow.wechatpay.plugin.api*

#忽略警告
-ignorewarnings

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keepclassmembers class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# for webview
-keepclassmembers class * {
    @android.webkit.JavascriptInterface
    <methods>;
}

-keep class android.webkit.** {
    <fields>;
    <methods>;
}

-keep interface  android.content.pm.** {
    <fields>;
    <methods>;
}

-keep class android.content.pm.** {
    <fields>;
    <methods>;
}

-keep class android.os.Process {
    <fields>;
    <methods>;
}

-keep class * extends java.io.Serializable

-keep class android.view.Window {
    <fields>;
    <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  *,* {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
#-keepclasseswithmembers,allowshrinking class *,* {
#    native <methods>;
#}

-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn androidx.annotation.Keep
#保留注解，如果不添加改行会导致我们的@Keep注解失效
-keepattributes *Annotation*
-keep @androidx.annotation.Keep class **{
@androidx.annotation.Keep <fields>;
@androidx.annotation.Keep <methods>;
}

-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}

#
##*****   adjust   ***#
-keep class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.** { *; }
##*****   adjust   ***#

-keep class com.appsflyer.** { *; }

-keep class com.google.android.** { *; }

-keep class com.buildbox.**{*;}
-keep class com.secrethq.**{*;}
-keep class org.**{*;}
