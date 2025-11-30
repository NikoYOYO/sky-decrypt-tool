# TODO 清单

## 开源前准备

### 必须完成
- [x] 创建 README.md
- [x] 添加 LICENSE (MIT)
- [x] 完善 .gitignore
- [x] 创建贡献指南 (CONTRIBUTING.md)
- [ ] 添加应用截图 (重要)
  - [ ] screenshots/single_file.png
  - [ ] screenshots/batch_decrypt.png
  - [ ] screenshots/about_page.png
- [ ] 测试应用 (重要)
  - [ ] 单文件解密测试
  - [ ] 批量解密测试
  - [ ] 不同 Android 版本测试
  - [ ] 边界情况测试
- [ ] 构建 Release APK
  - [ ] 生成签名密钥
  - [ ] 配置签名
  - [ ] 构建并测试

### 可选完成
- [x] GitHub Actions 工作流
- [x] Issue 模板
- [x] PR 模板
- [x] 发布说明模板
- [ ] 添加徽章到 README
- [ ] 录制演示视频
- [ ] 多语言支持（英文）

---

## 发布流程

### 1. 本地准备
```bash
# 清理项目
./gradlew clean

# 运行测试
./gradlew test

# 检查代码
./gradlew lint

# 构建 Release
./gradlew assembleRelease
```

### 2. 初始化 Git
```bash
cd c:\Users\Another\Desktop\sky\decrypt_tool
git init
git add .
git commit -m "Initial commit: Sky Studio decrypt tool v1.0.0"
```

### 3. 创建 GitHub 仓库
- 仓库名: `sky-decrypt-tool`
- 描述: `Sky Studio 加密谱子解密工具 - Android 应用`
- 可见性: Public
- 不勾选 Initialize with README

### 4. 推送到 GitHub
```bash
git remote add origin https://github.com/NikoYOYO/sky-decrypt-tool.git
git branch -M main
git push -u origin main
```

### 5. 创建 Release
- Tag: `v1.0.0`
- Title: `v1.0.0 - 首个正式版本`
- 描述: 复制 RELEASE_NOTES.md
- 上传: app-release.apk

### 6. 配置仓库
- 添加 Topics: `android`, `sky-studio`, `decrypt`, `material-design`, `material-design-3`
- 设置主页: 项目主页 URL
- 启用 Issues
- 启用 Discussions（可选）

---

## 后续改进

### v1.1.0 计划
- [ ] 文件加密功能
- [ ] 格式验证
- [ ] 性能优化
- [ ] 自定义输出路径
- [ ] 历史记录

### v1.2.0 计划
- [ ] 云端同步
- [ ] 文件管理
- [ ] 更多格式支持
- [ ] 备份功能

### UI/UX 改进
- [ ] 添加空状态提示
- [ ] 改进错误提示
- [ ] 添加帮助文档
- [ ] 优化动画效果

### 文档改进
- [ ] 添加 FAQ
- [ ] 录制使用教程
- [ ] 翻译英文文档
- [ ] 添加架构文档

---

## 推广计划

### 立即执行
- [ ] GitHub Trending
- [ ] 社交媒体分享
- [ ] 相关社区发布

### 持续维护
- [ ] 及时响应 Issues
- [ ] 定期发布更新
- [ ] 收集用户反馈
- [ ] 改进功能

---

## 注意事项

### 安全
- 已从代码删除调试日志
- 已使用 Native 保护密钥
- .gitignore 已配置密钥文件
- 不要提交签名密钥到 Git

### 隐私
- 不收集用户数据
- 不需要网络权限
- 不需要敏感权限

### 法律
- MIT 开源协议
- 免责声明已添加
- 仅供学习使用

---

## 检查清单

发布前最后检查：

- [ ] 所有代码已提交
- [ ] README 完整准确
- [ ] 截图已添加
- [ ] LICENSE 文件存在
- [ ] .gitignore 正确配置
- [ ] 构建成功无警告
- [ ] 应用测试通过
- [ ] 签名配置正确
- [ ] 版本号正确
- [ ] 发布说明完整

完成上述检查后即可发布！
