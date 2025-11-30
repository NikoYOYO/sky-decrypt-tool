#include <jni.h>
#include <string>

static const unsigned char enc_ragus[] = {
    0x0E, 0x18, 0x76, 0x08, 0x7C, 0x0B, 0x27, 0x77,
    0x0F, 0x16, 0x1C, 0x02, 0x1C, 0x6D, 0x67, 0x21,
    0x34, 0x0F, 0x6D, 0x2C, 0x65, 0x3C, 0x23, 0x79,
    0x11, 0x32, 0x28, 0x63, 0x17, 0x32, 0x2F, 0x2F
};

static const unsigned char enc_tlas[] = {
    0x20, 0x2E, 0x18, 0x05, 0x31, 0x3B, 0x1C, 0x3F,
    0x0B, 0x3F, 0x75, 0x2D, 0x3B, 0x62, 0x11, 0x2B,
    0x21, 0x28, 0x05, 0x30, 0x20, 0x7B, 0x28, 0x67,
    0x0A, 0x07, 0x73, 0x32, 0x0B, 0x16, 0x72, 0x3C
};

static const unsigned char xor_key = 0x5A;

static inline std::string decrypt_key(const unsigned char* data, size_t len) {
    std::string result;
    result.reserve(len);
    
    for (size_t i = 0; i < len; i++) {
        result += (char)(data[i] ^ xor_key);
    }
    
    return result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_zhiduoshao_tool_NativeKeyProvider_getRAGUS(
        JNIEnv* env,
        jclass clazz) {
    std::string key = decrypt_key(enc_ragus, sizeof(enc_ragus));
    return env->NewStringUTF(key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_zhiduoshao_tool_NativeKeyProvider_getTLAS(
        JNIEnv* env,
        jclass clazz) {
    std::string key = decrypt_key(enc_tlas, sizeof(enc_tlas));
    return env->NewStringUTF(key.c_str());
}

