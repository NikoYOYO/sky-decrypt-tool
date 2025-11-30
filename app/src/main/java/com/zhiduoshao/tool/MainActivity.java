package com.zhiduoshao.tool;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText inputFileEdit;
    private MaterialButton selectFileBtn;
    private MaterialButton selectFolderBtn;
    private MaterialButton decryptBtn;
    private MaterialButton clearLogBtn;
    private MaterialButton menuBtn;
    private MaterialCardView logCard;
    private ScrollView logScrollView;
    private TextView logTextView;
    private LinearProgressIndicator progressIndicator;

    private Uri selectedUri = null;
    private boolean isDirectory = false;
    private StringBuilder logBuilder = new StringBuilder();

    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        handleFileSelection(uri, false);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> folderPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        handleFileSelection(uri, true);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        checkPermissions();
    }

    private void initViews() {
        inputFileEdit = findViewById(R.id.inputFileEdit);
        selectFileBtn = findViewById(R.id.selectFileBtn);
        selectFolderBtn = findViewById(R.id.selectFolderBtn);
        decryptBtn = findViewById(R.id.decryptBtn);
        clearLogBtn = findViewById(R.id.clearLogBtn);
        menuBtn = findViewById(R.id.menuBtn);
        logCard = findViewById(R.id.logCard);
        logScrollView = findViewById(R.id.logScrollView);
        logTextView = findViewById(R.id.logTextView);
        progressIndicator = findViewById(R.id.progressIndicator);

        logScrollView.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        selectFileBtn.setOnClickListener(v -> openFilePicker());
        selectFolderBtn.setOnClickListener(v -> openFolderPicker());
        decryptBtn.setOnClickListener(v -> startDecrypt());
        clearLogBtn.setOnClickListener(v -> {
            clearLog();
            addLog("Sky Studio 解密工具 v1.0");
            addLog("━━━━━━━━━━━━━━━━━━━━");
            addLog("点击「选择文件」或「选择目录」开始");
            addLog("支持单文件解密和批量解密");
            addLog("");
            Toast.makeText(this, "日志已清除", Toast.LENGTH_SHORT).show();
        });

        menuBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        decryptBtn.setEnabled(false);
        progressIndicator.setVisibility(View.GONE);
        
        addLog("Sky Studio 解密工具 v1.0");
        addLog("━━━━━━━━━━━━━━━━━━━━");
        addLog("点击「选择文件」或「选择目录」开始");
        addLog("支持单文件解密和批量解密");
        addLog("");
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                addLog("[!] 请授予存储权限");
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        REQUEST_STORAGE_PERMISSION);
            }
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(intent);
    }

    private void openFolderPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        folderPickerLauncher.launch(intent);
    }

    private String getDisplayPath(Uri uri) {
        try {
            String uriString = uri.toString();
            
            if (uriString.contains("/tree/")) {
                String path = uriString.substring(uriString.indexOf("/tree/") + 6);
                path = Uri.decode(path);
                
                if (path.contains("primary:")) {
                    path = path.replace("primary:", "/storage/emulated/0/");
                } else if (path.contains(":")) {
                    String[] parts = path.split(":", 2);
                    if (parts.length > 1) {
                        path = "/storage/" + parts[0] + "/" + parts[1];
                    }
                }
                
                return path;
            } else if (uriString.contains("/document/")) {
                String path = uriString.substring(uriString.indexOf("/document/") + 10);
                path = Uri.decode(path);
                
                if (path.contains("primary:")) {
                    path = path.replace("primary:", "/storage/emulated/0/");
                } else if (path.contains(":")) {
                    String[] parts = path.split(":", 2);
                    if (parts.length > 1) {
                        path = "/storage/" + parts[0] + "/" + parts[1];
                    }
                }
                
                return path;
            }
            
            return uri.getLastPathSegment();
        } catch (Exception e) {
            return uri.getLastPathSegment();
        }
    }

    private void handleFileSelection(Uri uri, boolean isFolder) {
        try {
            if (uri == null) {
                Toast.makeText(this, "无法获取URI", Toast.LENGTH_SHORT).show();
                return;
            }

            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            selectedUri = uri;
            isDirectory = isFolder;
            
            if (isFolder) {
                DocumentFile dir = DocumentFile.fromTreeUri(this, uri);
                if (dir != null && dir.exists()) {
                    int count = 0;
                    for (DocumentFile file : dir.listFiles()) {
                        if (file.isFile() && file.getName() != null && 
                            file.getName().toLowerCase().endsWith(".txt")) {
                            count++;
                        }
                    }
                    String displayPath = getDisplayPath(uri);
                    inputFileEdit.setText(displayPath);
                    decryptBtn.setEnabled(true);
                    addLog("[+] 已选择目录: " + displayPath);
                    addLog("[*] 找到 " + count + " 个 .txt 文件");
                } else {
                    Toast.makeText(this, "无法访问目录", Toast.LENGTH_SHORT).show();
                    addLog("[-] 目录无效");
                }
            } else {
                DocumentFile file = DocumentFile.fromSingleUri(this, uri);
                if (file != null && file.exists()) {
                    String displayPath = getDisplayPath(uri);
                    inputFileEdit.setText(displayPath);
                    decryptBtn.setEnabled(true);
                    addLog("[+] 已选择文件: " + displayPath);
                } else {
                    Toast.makeText(this, "无法访问文件", Toast.LENGTH_SHORT).show();
                    addLog("[-] 文件无效");
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "选择失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            addLog("[-] 选择错误: " + e.getMessage());
        }
    }

    private void startDecrypt() {
        if (selectedUri == null) {
            Toast.makeText(this, "请先选择文件或目录", Toast.LENGTH_SHORT).show();
            return;
        }

        setUIEnabled(false);
        progressIndicator.setVisibility(View.VISIBLE);
        progressIndicator.setIndeterminate(false);
        progressIndicator.setProgress(0);
        clearLog();

        if (isDirectory) {
            batchDecrypt();
        } else {
            singleDecrypt();
        }
    }

    private void singleDecrypt() {
        new Thread(() -> {
            DocumentFile file = DocumentFile.fromSingleUri(this, selectedUri);
            if (file == null || !file.exists()) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
                    setUIEnabled(true);
                });
                return;
            }
            
            decryptDocumentFile(file, new DecryptUtil.DecryptCallback() {
                @Override
                public void onLog(String message) {
                    runOnUiThread(() -> addLog(message));
                }

                @Override
                public void onProgress(int current, int total) {
                    runOnUiThread(() -> {
                        int progress = (int) ((current * 100.0) / total);
                        progressIndicator.setProgress(progress);
                    });
                }

                @Override
                public void onSuccess(String outputPath) {
                    runOnUiThread(() -> {
                        progressIndicator.setVisibility(View.GONE);
                        setUIEnabled(true);
                        Toast.makeText(MainActivity.this, "解密成功！", Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        addLog("错误: " + error);
                        progressIndicator.setVisibility(View.GONE);
                        setUIEnabled(true);
                        Toast.makeText(MainActivity.this, "解密失败: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }).start();
    }

    private void decryptDocumentFile(DocumentFile file, DecryptUtil.DecryptCallback callback) {
        try {
            callback.onLog("[*] 开始解密: " + file.getName());
            
            InputStream inputStream = getContentResolver().openInputStream(file.getUri());
            if (inputStream == null) {
                callback.onError("无法读取文件");
                return;
            }

            java.nio.charset.Charset charset = detectCharset(inputStream, callback);
            inputStream.close();
            
            inputStream = getContentResolver().openInputStream(file.getUri());
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            
            String content = new String(bytes, charset);

            String result = DecryptUtil.decryptContent(content, callback);
            
            if (result != null) {
                OutputStream outputStream = getContentResolver().openOutputStream(file.getUri(), "wt");
                if (outputStream != null) {
                    outputStream.write(result.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                    outputStream.close();
                    callback.onSuccess(file.getName());
                } else {
                    callback.onError("无法写入文件");
                }
            }
        } catch (Exception e) {
            callback.onError(e.getMessage() != null ? e.getMessage() : "未知错误");
        }
    }

    private java.nio.charset.Charset detectCharset(InputStream inputStream, DecryptUtil.DecryptCallback callback) throws Exception {
        byte[] bom = new byte[4];
        int n = inputStream.read(bom, 0, 4);
        
        if (n >= 2 && bom[0] == (byte)0xFF && bom[1] == (byte)0xFE) {
            callback.onLog("[*] 检测到 UTF-16LE 编码");
            return java.nio.charset.StandardCharsets.UTF_16LE;
        }
        if (n >= 2 && bom[0] == (byte)0xFE && bom[1] == (byte)0xFF) {
            callback.onLog("[*] 检测到 UTF-16BE 编码");
            return java.nio.charset.StandardCharsets.UTF_16BE;
        }
        if (n >= 3 && bom[0] == (byte)0xEF && bom[1] == (byte)0xBB && bom[2] == (byte)0xBF) {
            callback.onLog("[*] 检测到 UTF-8 (BOM) 编码");
            return java.nio.charset.StandardCharsets.UTF_8;
        }
        
        callback.onLog("[*] 使用默认 UTF-8 编码");
        return java.nio.charset.StandardCharsets.UTF_8;
    }

    private void batchDecrypt() {
        new Thread(() -> {
            DocumentFile dir = DocumentFile.fromTreeUri(this, selectedUri);
            if (dir == null || !dir.exists()) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "目录不存在", Toast.LENGTH_SHORT).show();
                    setUIEnabled(true);
                });
                return;
            }

            java.util.List<DocumentFile> txtFiles = new java.util.ArrayList<>();
            for (DocumentFile file : dir.listFiles()) {
                if (file.isFile() && file.getName() != null && 
                    file.getName().toLowerCase().endsWith(".txt")) {
                    txtFiles.add(file);
                }
            }
            
            if (txtFiles.isEmpty()) {
                runOnUiThread(() -> {
                    addLog("[!] 目录中没有找到 .txt 文件");
                    progressIndicator.setVisibility(View.GONE);
                    setUIEnabled(true);
                    Toast.makeText(this, "目录中没有 .txt 文件", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            int total = txtFiles.size();
            final int[] successCount = {0};
            final int[] failCount = {0};
            final StringBuilder failedFiles = new StringBuilder();

            runOnUiThread(() -> {
                addLog("━━━━━━━━━━━━━━━━━━━━");
                addLog("[*] 开始批量解密");
                addLog("[*] 共找到 " + total + " 个文件");
                addLog("━━━━━━━━━━━━━━━━━━━━");
                addLog("");
            });

            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(4);
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(total);

            for (int i = 0; i < total; i++) {
                DocumentFile file = txtFiles.get(i);
                int index = i + 1;
                
                executor.execute(() -> {
                    try {
                        final boolean[] success = {false};
                        final String[] errorMsg = {""};
                        
                        decryptDocumentFile(file, new DecryptUtil.DecryptCallback() {
                                @Override
                                public void onLog(String message) {
                                }

                                @Override
                                public void onProgress(int current, int total) {
                                }

                                @Override
                                public void onSuccess(String outputPath) {
                                    success[0] = true;
                                }

                                @Override
                                public void onError(String error) {
                                    errorMsg[0] = error;
                                }
                            });
                        
                        if (success[0]) {
                            synchronized (successCount) {
                                successCount[0]++;
                            }
                            runOnUiThread(() -> addLog("[✓] (" + index + "/" + total + ") " + file.getName()));
                        } else {
                            synchronized (failCount) {
                                failCount[0]++;
                                if (failedFiles.length() > 0) failedFiles.append("\n");
                                failedFiles.append(file.getName()).append(" - ").append(errorMsg[0]);
                            }
                            runOnUiThread(() -> addLog("[✗] (" + index + "/" + total + ") " + file.getName() + " - " + errorMsg[0]));
                        }
                    } catch (Exception e) {
                        synchronized (failCount) {
                            failCount[0]++;
                            if (failedFiles.length() > 0) failedFiles.append("\n");
                            failedFiles.append(file.getName()).append(" - ").append(e.getMessage());
                        }
                        runOnUiThread(() -> addLog("[✗] (" + index + "/" + total + ") " + file.getName() + " - " + e.getMessage()));
                    } finally {
                        runOnUiThread(() -> {
                            int progress = (int) (((total - latch.getCount() + 1) * 100.0) / total);
                            progressIndicator.setProgress(progress);
                        });
                        latch.countDown();
                    }
                });
            }

            try {
                latch.await();
                executor.shutdown();
                
                runOnUiThread(() -> {
                    addLog("");
                    addLog("━━━━━━━━━━━━━━━━━━━━");
                    addLog("[*] 批量解密完成");
                    addLog("━━━━━━━━━━━━━━━━━━━━");
                    addLog("[+] 总文件数: " + total);
                    addLog("[+] 成功: " + successCount[0]);
                    addLog("[+] 失败: " + failCount[0]);
                    
                    if (failCount[0] > 0) {
                        addLog("");
                        addLog("[失败文件列表]:");
                        String[] fails = failedFiles.toString().split("\n");
                        for (String fail : fails) {
                            addLog("  - " + fail);
                        }
                    }
                    
                    progressIndicator.setVisibility(View.GONE);
                    setUIEnabled(true);
                    
                    String message = "完成！成功: " + successCount[0] + ", 失败: " + failCount[0];
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                });
            } catch (InterruptedException e) {
                runOnUiThread(() -> {
                    addLog("[!] 批量解密被中断");
                    progressIndicator.setVisibility(View.GONE);
                    setUIEnabled(true);
                });
            }
        }).start();
    }

    private void setUIEnabled(boolean enabled) {
        selectFileBtn.setEnabled(enabled);
        selectFolderBtn.setEnabled(enabled);
        decryptBtn.setEnabled(enabled && selectedUri != null);
    }

    private void addLog(String message) {
        logBuilder.append(message).append("\n");
        logTextView.setText(logBuilder.toString());
    }

    private void clearLog() {
        logBuilder.setLength(0);
        logTextView.setText("");
    }
}