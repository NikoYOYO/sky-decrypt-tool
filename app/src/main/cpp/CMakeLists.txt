cmake_minimum_required(VERSION 3.22.1)

project("keyprovider")

# 创建共享库
add_library(
    keyprovider
    SHARED
    key_provider.cpp
)

# 查找日志库
find_library(
    log-lib
    log
)

# 链接库
target_link_libraries(
    keyprovider
    ${log-lib}
)
