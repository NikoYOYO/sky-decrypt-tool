# ProGuard配置 - 密钥保护
# 添加到 app/proguard-rules.pro

# 1. 开启字符串加密
-obfuscationdictionary proguard-dict.txt
-classobfuscationdictionary proguard-dict.txt
-packageobfuscationdictionary proguard-dict.txt

# 2. 混淆字段名
-repackageclasses 'a.b.c'
-allowaccessmodification

# 3. 优化（移除未使用代码）
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5

# 4. 混淆密钥相关类
-keep class com.zhiduoshao.tool.DecryptUtil {
    # 保留公共方法
    public *;
}

# 但混淆私有字段
-keepclassmembers class com.zhiduoshao.tool.DecryptUtil {
    private static final java.lang.String RAGUS;
    private static final java.lang.String TLAS;
}

# 5. 字符串加密（需要第三方插件）
# 使用 DexGuard 或 Guardsquare

# 6. 控制流混淆
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# 7. 反射保护
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# 8. 移除日志
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
