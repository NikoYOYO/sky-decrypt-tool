# 贡献指南

感谢你考虑为 Sky Studio 解密工具做出贡献！

## 如何贡献

### 报告 Bug

如果你发现了 Bug，请创建一个 Issue 并包含以下信息：

- 标题：简短描述问题
- 环境：Android 版本、设备型号
- 重现步骤：详细的操作步骤
- 预期行为：应该发生什么
- 实际行为：实际发生了什么
- 日志：相关的日志信息
- 截图：如果可能，提供截图

### 提交功能建议

如果你有新功能的想法：

1. 先检查是否已有相关 Issue
2. 创建新 Issue，标记为 `enhancement`
3. 清楚描述功能的用途和实现方式

### 提交代码

#### 开发流程

1. **Fork 仓库**
   ```bash
   # 在 GitHub 上点击 Fork 按钮
   ```

2. **克隆你的 Fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/sky-decrypt-tool.git
   cd sky-decrypt-tool
   ```

3. **创建分支**
   ```bash
   git checkout -b feature/amazing-feature
   # 或
   git checkout -b fix/bug-description
   ```

4. **开发和测试**
   - 编写代码
   - 运行测试
   - 手动测试

5. **提交更改**
   ```bash
   git add .
   git commit -m "Add some amazing feature"
   ```

6. **推送到 GitHub**
   ```bash
   git push origin feature/amazing-feature
   ```

7. **创建 Pull Request**
   - 在 GitHub 上打开 Pull Request
   - 填写 PR 模板
   - 等待审核

#### 代码规范

**Java 代码**
- 遵循 Google Java Style Guide
- 使用有意义的变量名
- 添加必要的注释
- 保持方法简短（<50行）

**XML 布局**
- 使用语义化的 ID
- 保持布局层级简单
- 使用 ConstraintLayout 优化性能
- 删除无用的注释

**C++ 代码**
- 遵循 C++ Core Guidelines
- 使用 RAII 管理资源
- 避免内存泄漏

#### Commit 规范

使用语义化的 commit 消息：

```
<type>: <subject>

<body>

<footer>
```

Type 类型：
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式（不影响功能）
- `refactor`: 重构
- `test`: 添加测试
- `chore`: 构建/工具变动

示例：
```
feat: 添加批量解密功能

- 支持选择目录
- 多线程并发处理
- 显示详细统计信息

Closes #123
```

### 代码审查

所有 PR 都需要审查：

1. **自动检查**
   - 构建成功
   - 代码格式检查
   - 静态分析

2. **人工审查**
   - 代码质量
   - 功能正确性
   - 性能影响

3. **测试**
   - 单元测试（如有）
   - 手动测试

### 开发环境

#### 必需工具

- **JDK**: 17+
- **Android Studio**: 最新稳定版
- **NDK**: 25.1.8937393+
- **Git**: 2.0+

#### 推荐插件

- Android Studio
  - `.ignore` - Git 忽略文件管理
  - `Key Promoter X` - 快捷键提示
  - `Rainbow Brackets` - 彩色括号

#### 本地测试

```bash
# 运行 lint 检查
./gradlew lint

# 构建 Debug 版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

## 项目结构

```
sky-decrypt-tool/
├── app/src/main/
│   ├── java/              # Java 源代码
│   ├── cpp/               # C++ Native 代码
│   ├── res/               # 资源文件
│   │   ├── layout/        # 布局文件
│   │   ├── drawable/      # 图片资源
│   │   └── values/        # 值资源
│   └── AndroidManifest.xml
├── gradle/                # Gradle 配置
├── .gitignore
├── LICENSE
├── README.md
└── CONTRIBUTING.md
```

## 行为准则

### 我们的承诺

为了营造开放和友好的环境，我们承诺：

- 使用友好和包容的语言
- 尊重不同的观点和经验
- 优雅地接受建设性批评
- 关注对社区最有利的事情
- 对其他社区成员表示同理心

### 不可接受的行为

- 使用性别化语言或图像
- 人身攻击或政治攻击
- 公开或私下骚扰
- 未经许可发布他人信息
- 其他不专业或不受欢迎的行为

## 问题与讨论

- **Bug 报告**: [Issues](../../issues)
- **功能建议**: [Issues](../../issues)
- **一般讨论**: [Discussions](../../discussions)

## 获取帮助

如果你在贡献过程中遇到问题：

1. 查看 [README.md](README.md)
2. 搜索现有 Issues
3. 创建新 Issue 寻求帮助
4. 联系维护者：QQ 2227235998

## 许可证

提交代码即表示你同意将代码以 MIT 许可证发布。

---

再次感谢你的贡献！
