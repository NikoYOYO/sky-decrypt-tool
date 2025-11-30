package com.zhiduoshao.tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DecryptUtil {

    private static String getRAGUS() {
        return NativeKeyProvider.getRAGUS();
    }
    
    private static String getTLAS() {
        return NativeKeyProvider.getTLAS();
    }

    public interface DecryptCallback {
        void onLog(String message);
        void onProgress(int current, int total);
        void onSuccess(String outputPath);
        void onError(String error);
    }

    public static String decryptContent(String jsonContent, DecryptCallback callback) {
        try {
            callback.onLog("[*] 开始解密...");
            callback.onLog("[*] 文件大小: " + jsonContent.length() + " 字符");

            callback.onLog("[*] 解析JSON...");
            JSONArray dataArray = new JSONArray(jsonContent);
            
            if (dataArray.length() == 0) {
                callback.onError("文件格式错误：空数组");
                return null;
            }

            JSONObject song = dataArray.getJSONObject(0);

            boolean isEncrypted = song.optBoolean("isEncrypted", false);
            callback.onLog("[*] isEncrypted: " + isEncrypted);
            
            if (!isEncrypted) {
                callback.onError("文件未加密，无需解密");
                return null;
            }

            callback.onLog("[*] 检测到加密文件");

            JSONArray encryptedNotes = song.getJSONArray("songNotes");
            int totalNotes = encryptedNotes.length();
            callback.onLog("[*] 加密数据长度: " + totalNotes);

            callback.onLog("[*] 解密中...");
            String decryptedJson = decrypt(encryptedNotes, callback);

            callback.onLog("[*] 解析解密数据...");
            JSONArray notesArray = new JSONArray(decryptedJson);

            JSONObject plaintext = new JSONObject();
            JSONArray names = song.names();
            if (names != null) {
                for (int i = 0; i < names.length(); i++) {
                    String key = names.getString(i);
                    if (!key.equals("isEncrypted") && 
                        !key.equals("keyVersion") && 
                        !key.equals("songNotes")) {
                        plaintext.put(key, song.get(key));
                    }
                }
            }

            plaintext.put("isEncrypted", false);
            plaintext.put("songNotes", notesArray);

            JSONArray outputArray = new JSONArray();
            outputArray.put(plaintext);

            callback.onLog("━━━━━━━━━━━━━━━━━━━━");
            callback.onLog("[成功] 解密完成");
            callback.onLog("━━━━━━━━━━━━━━━━━━━━");
            callback.onLog("[+] 音符数量: " + notesArray.length());
            
            String result = outputArray.toString();
            callback.onLog("[*] 解密后大小: " + result.length() + " 字符");
            return result;

        } catch (JSONException e) {
            callback.onLog("[!] JSON解析错误");
            callback.onError("JSON解析错误: " + e.getMessage());
            return null;
        } catch (Exception e) {
            callback.onLog("[!] 发生异常: " + e.getClass().getSimpleName());
            callback.onError("未知错误: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void decryptFile(String inputPath, String outputPath, DecryptCallback callback) {
        try {
            callback.onLog("[*] 开始解密...");
            callback.onLog("[*] 输入文件: " + inputPath);
            
            if (inputPath.equals(outputPath)) {
                callback.onLog("[!] 警告: 将覆盖原文件");
            }

            callback.onLog("[*] 读取文件...");
            String jsonContent = readFile(inputPath);
            callback.onLog("[*] 文件大小: " + jsonContent.length() + " 字符");

            callback.onLog("[*] 解析JSON...");
            JSONArray dataArray = new JSONArray(jsonContent);
            
            if (dataArray.length() == 0) {
                callback.onError("文件格式错误：空数组");
                return;
            }

            JSONObject song = dataArray.getJSONObject(0);

            boolean isEncrypted = song.optBoolean("isEncrypted", false);
            callback.onLog("[*] isEncrypted: " + isEncrypted);
            
            if (!isEncrypted) {
                callback.onLog("━━━━━━━━━━━━━━━━━━━━");
                callback.onLog("[警告] 该文件已经是解密状态");
                callback.onLog("━━━━━━━━━━━━━━━━━━━━");
                callback.onLog("");
                callback.onLog("提示：");
                callback.onLog("  - 请选择加密文件");
                callback.onLog("  - 加密文件的 isEncrypted 字段为 true");
                callback.onLog("  - 加密文件的 songNotes 是数字数组");
                callback.onLog("");
                callback.onError("文件未加密，无需解密");
                return;
            }

            callback.onLog("[*] 检测到加密文件");

            JSONArray encryptedNotes = song.getJSONArray("songNotes");
            int totalNotes = encryptedNotes.length();
            callback.onLog("[*] 加密数据长度: " + totalNotes);

            callback.onLog("[*] 解密中...");
            String decryptedJson = decrypt(encryptedNotes, callback);

            callback.onLog("[*] 解析解密数据...");
            JSONArray notesArray = new JSONArray(decryptedJson);

            JSONObject plaintext = new JSONObject();
            JSONArray names = song.names();
            if (names != null) {
                for (int i = 0; i < names.length(); i++) {
                    String key = names.getString(i);
                    if (!key.equals("isEncrypted") && 
                        !key.equals("keyVersion") && 
                        !key.equals("songNotes")) {
                        plaintext.put(key, song.get(key));
                    }
                }
            }

            plaintext.put("isEncrypted", false);
            plaintext.put("songNotes", notesArray);

            JSONArray outputArray = new JSONArray();
            outputArray.put(plaintext);

            callback.onLog("[*] 写入文件...");
            writeFile(outputPath, outputArray.toString(), callback);

            callback.onLog("━━━━━━━━━━━━━━━━━━━━");
            callback.onLog("[成功] 解密完成");
            callback.onLog("━━━━━━━━━━━━━━━━━━━━");
            callback.onLog("");
            if (inputPath.equals(outputPath)) {
                callback.onLog("[+] 原文件已被覆盖");
            }
            callback.onLog("[+] 文件路径: " + outputPath);
            callback.onLog("[+] 音符数量: " + notesArray.length());
            callback.onLog("");
            callback.onLog("提示: 现在可以在Sky Studio中导入此文件了");
            
            callback.onSuccess(outputPath);

        } catch (JSONException e) {
            callback.onLog("━━━━━━━━━━━━━━━━━━━━");
            callback.onLog("[错误] JSON解析失败");
            callback.onLog("━━━━━━━━━━━━━━━━━━━━");
            callback.onLog("");
            callback.onLog("错误信息: " + e.getMessage());
            callback.onLog("");
            callback.onLog("可能的原因：");
            callback.onLog("  1. 文件编码不正确");
            callback.onLog("  2. 文件已损坏");
            callback.onLog("  3. 不是Sky Studio文件");
            callback.onLog("  4. 选择了错误的文件");
            callback.onLog("");
            callback.onLog("建议：");
            callback.onLog("  - 确保选择的是.txt格式的文件");
            callback.onLog("  - 尝试重新导出文件");
            callback.onLog("  - 检查文件名是否包含特殊字符");
            callback.onError("JSON解析错误: " + e.getMessage());
        } catch (IOException e) {
            callback.onLog("[错误] 文件读写错误: " + e.getMessage());
            callback.onLog("");
            callback.onLog("可能的原因：");
            callback.onLog("  - 文件不存在或已被删除");
            callback.onLog("  - 没有读取权限");
            callback.onLog("  - 文件正在被其他应用使用");
            callback.onError("文件读写错误: " + e.getMessage());
        } catch (Exception e) {
            callback.onLog("[错误] 未知错误: " + e.getMessage());
            callback.onLog("");
            callback.onLog("堆栈信息:");
            for (StackTraceElement element : e.getStackTrace()) {
                callback.onLog("  " + element.toString());
                if (callback != null) break;
            }
            callback.onError("未知错误: " + e.getMessage());
        }
    }

    private static String decrypt(JSONArray shortList, DecryptCallback callback) throws JSONException {
        StringBuilder result = new StringBuilder();
        int length = shortList.length();

        for (int i = 0; i < length; i++) {
            int val = shortList.getInt(i);
            
            String ragus = getRAGUS();
            char keyChar = ragus.charAt(i % ragus.length());
            int decrypted = val - (int)keyChar + 100;

            if (decrypted >= 0 && decrypted < 0x110000) {
                result.append((char)decrypted);
            } else {
                callback.onLog("[!] 警告: 位置 " + i + " 的字符无效: " + decrypted);
                result.append('?');
            }

            if (i % 1000 == 0) {
                callback.onProgress(i, length);
            }
        }

        callback.onProgress(length, length);

        String text = result.toString();
        String tlas = getTLAS();
        int tlasIndex = text.indexOf(tlas);
        if (tlasIndex != -1) {
            text = text.substring(0, tlasIndex);
        }

        return text;
    }

    private static String readFile(String path) throws IOException {
        byte[] bom = new byte[4];
        try (FileInputStream fis = new FileInputStream(path)) {
            int read = fis.read(bom);
            if (read >= 2) {
                if (bom[0] == (byte)0xFF && bom[1] == (byte)0xFE) {
                    return readFileWithEncoding(path, StandardCharsets.UTF_16LE);
                }
                if (bom[0] == (byte)0xFE && bom[1] == (byte)0xFF) {
                    return readFileWithEncoding(path, StandardCharsets.UTF_16BE);
                }
                if (read >= 3 && bom[0] == (byte)0xEF && bom[1] == (byte)0xBB && bom[2] == (byte)0xBF) {
                    return readFileWithEncoding(path, StandardCharsets.UTF_8);
                }
            }
        }
        
        try {
            String content = readFileWithEncoding(path, StandardCharsets.UTF_8);
            if (content.trim().startsWith("[") || content.trim().startsWith("{")) {
                return content;
            }
        } catch (Exception ignored) {
        }
        
        try {
            return readFileWithEncoding(path, StandardCharsets.UTF_16);
        } catch (Exception e) {
            return readFileWithEncoding(path, StandardCharsets.UTF_16LE);
        }
    }

    private static String readFileWithEncoding(String path, Charset charset) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), charset))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine && line.length() > 0 && line.charAt(0) == '\uFEFF') {
                    line = line.substring(1);
                }
                content.append(line);
                firstLine = false;
            }
        }
        return content.toString();
    }

    private static void writeFile(String path, String content, DecryptCallback callback) throws IOException {
        String compactJson = content
                .replaceAll("\\s*:\\s*", ":")
                .replaceAll("\\s*,\\s*", ",")
                .replaceAll("\\s*\\{\\s*", "{")
                .replaceAll("\\s*\\}\\s*", "}")
                .replaceAll("\\s*\\[\\s*", "[")
                .replaceAll("\\s*\\]\\s*", "]");

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(path), StandardCharsets.UTF_8)) {
            writer.write(compactJson);
        }

        callback.onLog("[*] 文件已保存为UTF-8格式");
    }

    public static String encrypt(String text) {
        text += getTLAS();
        JSONArray result = new JSONArray();

        String ragus = getRAGUS();
        for (int i = 0; i < text.length(); i++) {
            char plainChar = text.charAt(i);
            char keyChar = ragus.charAt(i % ragus.length());
            int encrypted = (int)plainChar + (int)keyChar - 100;
            result.put(encrypted);
        }

        return result.toString();
    }
}
