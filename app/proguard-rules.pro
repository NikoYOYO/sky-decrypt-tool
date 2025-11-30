# ============================================================
# ProGuard基本配置
# ============================================================

# 保留Native方法（必需，否则JNI调用失败）
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# 保留Android基础组件
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# 保留属性
-keepattributes Exceptions,InnerClasses,Signature,SourceFile,LineNumberTable