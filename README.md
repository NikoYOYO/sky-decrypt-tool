# Sky Studio 解密工具

<div align="center">

![Logo](app/src/main/res/drawable/app_logo.jpg)

**一款用于解密 Sky Studio 加密谱子文件的 Android 应用**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)

[功能特性](#功能特性) • [下载安装](#下载安装) • [使用说明](#使用说明) • [技术实现](#技术实现) • [构建指南](#构建指南) • [开源协议](#开源协议)

</div>

---

## 功能特性

### 核心功能
- **一键解密** - 支持解密 Sky Studio 加密的 `.txt` 谱子文件
- **批量处理** - 选择目录批量解密多个文件，自动跳过失败
- **自动识别** - 智能检测文件编码（UTF-8 / UTF-16 LE / UTF-16 BE）
- **原地覆盖** - 解密后直接覆盖原文件，方便快捷
- **实时进度** - 显示解密进度和详细日志

### 界面设计
- **Material Design 3** - 现代化的界面设计
- **深色模式** - 跟随系统自动切换
- **流畅动画** - 精心设计的卡片动画
- **Android 15** - 完美支持最新系统

### 性能优化
- **多线程处理** - 4 线程并发批量解密
- **内存优化** - DocumentFile API 处理大文件
- **权限管理** - SAF 框架自动管理存储权限

---

## 下载安装

### 方式一：下载 APK
前往 [Releases](../../releases) 页面下载最新版本的 APK 文件。

### 方式二：自行编译
```bash
git clone https://github.com/NikoYOYO/sky-decrypt-tool.git
cd sky-decrypt-tool
./gradlew assembleRelease
```

**系统要求**
- Android 5.0 (API 21) 或更高版本
- 推荐 Android 11+ 以获得最佳体验

---

## 使用说明

### 单文件解密

1. **选择文件** - 点击「选择文件」按钮
2. **确认路径** - 查看文件路径是否正确
3. **开始解密** - 点击「开始解密」按钮
4. **查看日志** - 等待解密完成，查看详细日志

### 批量解密

1. **选择目录** - 点击「选择目录」按钮
2. **查看统计** - 显示找到的 `.txt` 文件数量
3. **批量处理** - 点击「开始解密」自动处理所有文件
4. **查看结果** - 显示成功/失败统计和详细列表

### 日志说明

```
━━━━━━━━━━━━━━━━━━━━
[*] 批量解密完成
━━━━━━━━━━━━━━━━━━━━
[+] 总文件数: 50
[+] 成功: 48
[+] 失败: 2

[失败文件列表]:
  - file1.txt - 文件未加密，无需解密
  - file2.txt - 文件格式错误
```

---

## 技术实现

### 解密算法

基于逆向分析的 XOR 对称加密算法：

```java
plaintext = (encrypted - key) + 100
```

**核心流程**：
1. 从 Native 层获取 XOR 密钥
2. 遍历加密数组进行解密计算
3. 移除签名后缀恢复明文
4. 转换为 UTF-8 编码输出

### 技术栈

| 组件 | 技术 |
|------|------|
| **UI 框架** | Material Design 3 |
| **编程语言** | Java + C++ (JNI) |
| **架构模式** | MVVM + Callback |
| **文件访问** | DocumentFile API (SAF) |
| **并发处理** | ExecutorService (4 线程池) |
| **编码检测** | BOM 自动识别 |
| **权限管理** | Scoped Storage |

### 项目结构

```
sky-decrypt-tool/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zhiduoshao/tool/
│   │   │   │   ├── MainActivity.java       # 主界面
│   │   │   │   ├── AboutActivity.java      # 关于页面
│   │   │   │   ├── DecryptUtil.java        # 解密工具类
│   │   │   │   ├── NativeKeyProvider.java  # Native 密钥接口
│   │   │   │   └── FileUtil.java           # 文件工具类
│   │   │   ├── cpp/
│   │   │   │   ├── key_provider.cpp        # Native 密钥实现
│   │   │   │   └── CMakeLists.txt          # NDK 构建配置
│   │   │   └── res/                        # 资源文件
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── gradle/
├── .gitignore
├── LICENSE
└── README.md
```

---

## 构建指南

### 环境要求

- **JDK**: 17 或更高版本
- **Android Studio**: Hedgehog (2023.1.1) 或更高
- **NDK**: 25.1.8937393 或更高版本
- **Gradle**: 8.2+
- **SDK**: API 34 (Android 14)

### 构建步骤

#### 1. 克隆项目
```bash
git clone https://github.com/NikoYOYO/sky-decrypt-tool.git
cd sky-decrypt-tool
```

#### 2. 配置 NDK
在 Android Studio 中：
- `Tools` → `SDK Manager` → `SDK Tools`
- 勾选 `NDK (Side by side)` 和 `CMake`

#### 3. 编译项目
```bash
# Debug 版本
./gradlew assembleDebug

# Release 版本（需要签名）
./gradlew assembleRelease
```

#### 4. 安装到设备
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

### 签名配置

创建 `keystore.properties` 文件（不要提交到 Git）：
```properties
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=your_key_alias
storeFile=your_keystore_file.jks
```

---

## 功能演示

### 单文件解密
![Single File](screenshots/single_file.png)

### 批量解密
![Batch Decrypt](screenshots/batch_decrypt.png)

### 关于页面
![About Page](screenshots/about_page.png)

---

## 常见问题

### 1. 无法访问路径？
**原因**：Android 11+ 使用分区存储
**解决**：使用应用内的文件/目录选择器

### 2. 解密失败？
**检查项**：
- ✅ 文件是否为加密文件（`isEncrypted: true`）
- ✅ 文件格式是否为 JSON 数组
- ✅ 文件是否损坏

### 3. 魅族手机报错？
这是系统安全服务的日志，不影响应用运行，可忽略。

---

## 贡献指南

欢迎提交 Issue 和 Pull Request！

### 开发流程
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码规范
- 遵循 Java 代码规范
- 保持代码简洁清晰
- 添加必要的注释
- 测试后再提交

---

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

```
MIT License

Copyright (c) 2025 知多少

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

**简单来说**：
- ✅ 允许商业使用
- ✅ 允许修改分发
- ✅ 允许私人使用
- ⚠️ 需保留版权声明

---

## 免责声明

本软件仅供**学习交流**使用，不得用于商业用途。

使用本软件产生的任何后果由使用者自行承担，开发者不承担任何法律责任。

---

## 作者

**知多少**
- QQ: 2227235998
- GitHub: [@NikoYOYO](https://github.com/NikoYOYO)

---

## 致谢

- [Sky Studio](https://play.google.com/store/apps/details?id=com.Maple.SkyStudio) - 原始应用
- [Material Design 3](https://m3.material.io/) - UI 设计规范
- Android 开源社区

---

## 项目状态

![GitHub stars](https://img.shields.io/github/stars/NikoYOYO/sky-decrypt-tool?style=social)
![GitHub forks](https://img.shields.io/github/forks/NikoYOYO/sky-decrypt-tool?style=social)
![GitHub issues](https://img.shields.io/github/issues/NikoYOYO/sky-decrypt-tool)
![GitHub license](https://img.shields.io/github/license/NikoYOYO/sky-decrypt-tool)

---

<div align="center">

**如果这个项目对你有帮助，请给一个 Star 支持一下！**

Made with Love by 知多少

</div>
