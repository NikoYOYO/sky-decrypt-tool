# Git 使用指南

本指南帮助你将项目推送到 GitHub。

## 1. 初始化 Git 仓库

```bash
cd c:\Users\Another\Desktop\sky\decrypt_tool
git init
```

## 2. 添加文件到暂存区

```bash
# 添加所有文件
git add .

# 或者选择性添加
git add README.md LICENSE .gitignore
git add app/
```

## 3. 提交更改

```bash
git commit -m "Initial commit: Sky Studio decrypt tool"
```

## 4. 在 GitHub 创建仓库

1. 登录 GitHub
2. 点击右上角 `+` → `New repository`
3. 填写信息：
   - **Repository name**: `sky-decrypt-tool`
   - **Description**: `Sky Studio 加密谱子解密工具 - Android 应用`
   - **Public** or **Private**: 选择公开
   - **不要勾选** `Initialize this repository with a README`
4. 点击 `Create repository`

## 5. 连接远程仓库

```bash
# 添加远程仓库
git remote add origin https://github.com/NikoYOYO/sky-decrypt-tool.git

# 验证远程仓库
git remote -v
```

## 6. 推送到 GitHub

```bash
# 重命名分支为 main
git branch -M main

# 推送到远程仓库
git push -u origin main
```

## 7. 后续更新

### 提交更改

```bash
# 查看状态
git status

# 添加更改
git add .

# 提交
git commit -m "描述你的更改"

# 推送
git push
```

### 创建新分支

```bash
# 创建并切换到新分支
git checkout -b feature/new-feature

# 推送新分支
git push -u origin feature/new-feature
```

### 合并分支

```bash
# 切换到 main 分支
git checkout main

# 合并分支
git merge feature/new-feature

# 推送
git push
```

## 8. 发布版本

### 创建标签

```bash
# 创建带注释的标签
git tag -a v1.0.0 -m "Release version 1.0.0"

# 推送标签
git push origin v1.0.0

# 或推送所有标签
git push --tags
```

### GitHub Release

1. 在 GitHub 仓库页面点击 `Releases`
2. 点击 `Create a new release`
3. 填写信息：
   - **Tag version**: `v1.0.0`
   - **Release title**: `v1.0.0 - 首个正式版本`
   - **Description**: 复制 `RELEASE_NOTES.md` 内容
4. 上传 APK 文件
5. 点击 `Publish release`

## 9. 配置 GitHub Secrets

如果要使用自动发布功能，需要配置密钥：

1. 进入仓库 `Settings` → `Secrets and variables` → `Actions`
2. 添加以下 secrets：
   - `SIGNING_KEY`: 签名密钥（Base64 编码）
   - `ALIAS`: 密钥别名
   - `KEY_STORE_PASSWORD`: Keystore 密码
   - `KEY_PASSWORD`: Key 密码

### 生成签名密钥 Base64

```bash
# Windows PowerShell
$bytes = [System.IO.File]::ReadAllBytes("path/to/your/keystore.jks")
[Convert]::ToBase64String($bytes)
```

## 10. 常用命令

```bash
# 查看提交历史
git log --oneline

# 查看文件差异
git diff

# 撤销未提交的更改
git checkout -- <file>

# 撤销最后一次提交（保留更改）
git reset --soft HEAD~1

# 撤销最后一次提交（删除更改）
git reset --hard HEAD~1

# 拉取远程更新
git pull

# 克隆仓库
git clone https://github.com/NikoYOYO/sky-decrypt-tool.git
```

## 11. .gitignore 说明

已配置的 `.gitignore` 会忽略：
- 构建文件 (`build/`, `*.apk`)
- IDE 配置 (`.idea/`, `*.iml`)
- 本地配置 (`local.properties`, `keystore.properties`)
- 签名密钥 (`*.jks`, `*.keystore`)
- 其他临时文件

## 12. 最佳实践

### Commit 消息规范

```bash
# 好的示例
git commit -m "feat: 添加批量解密功能"
git commit -m "fix: 修复编码检测问题"
git commit -m "docs: 更新 README"

# 不好的示例
git commit -m "update"
git commit -m "fix bug"
```

### 分支管理

```
main         - 稳定版本
develop      - 开发版本
feature/*    - 新功能分支
fix/*        - Bug 修复分支
release/*    - 发布准备分支
```

### 推送前检查

```bash
# 1. 运行测试
./gradlew test

# 2. 检查代码
./gradlew lint

# 3. 构建成功
./gradlew build

# 4. 提交推送
git push
```

## 13. 故障排除

### 推送失败

```bash
# 拉取最新更改
git pull origin main

# 解决冲突后推送
git push
```

### 忘记添加文件

```bash
# 修改最后一次提交
git add forgotten-file.txt
git commit --amend --no-edit
git push -f
```

### 取消跟踪文件

```bash
# 停止跟踪但保留本地文件
git rm --cached <file>

# 更新 .gitignore 后
git rm -r --cached .
git add .
git commit -m "Update .gitignore"
```

---

## 完成！

现在你的项目已经开源到 GitHub 了！

下一步：
1. 添加项目截图到 `screenshots/` 文件夹
2. 更新 README.md 中的截图链接
3. 创建第一个 Release
4. 宣传你的项目

推广渠道：
- GitHub Topics: 添加 `android`, `sky-studio`, `decrypt`, `material-design`
- 社交媒体分享
- 相关论坛发布

Good luck!
