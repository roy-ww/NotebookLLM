#!/bin/bash

# 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

echo "==================================="
echo "启动前端项目 - Notebook LLM"
echo "==================================="

# 检查 Node.js 是否安装
if ! command -v node &> /dev/null; then
    echo "错误: Node.js 未安装"
    echo "请先安装 Node.js 18 或更高版本"
    exit 1
fi

# 检查 Node.js 版本
NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 18 ]; then
    echo "错误: Node.js 版本过低"
    echo "需要 Node.js 18 或更高版本，当前版本: $(node -v)"
    exit 1
fi

echo "Node.js 版本: $(node -v)"
echo "npm 版本: $(npm -v)"

# 检查 pnpm 是否安装
if ! command -v pnpm &> /dev/null; then
    echo "检测到 pnpm 未安装，正在安装 pnpm..."
    npm install -g pnpm
    if [ $? -ne 0 ]; then
        echo "错误: pnpm 安装失败"
        exit 1
    fi
    echo "pnpm 安装成功"
fi

echo "pnpm 版本: $(pnpm -v)"

# 检查 node_modules 是否存在，如果不存在则安装依赖
if [ ! -d "node_modules" ]; then
    echo ""
    echo "检测到依赖未安装，正在安装依赖..."
    echo "这可能需要几分钟时间..."
    echo "==================================="
    pnpm install
    if [ $? -ne 0 ]; then
        echo "错误: 依赖安装失败"
        exit 1
    fi
    echo "依赖安装完成"
fi

echo ""
echo "==================================="
echo "正在启动开发服务器..."
echo "访问地址: http://localhost:3000"
echo "按 Ctrl+C 停止服务器"
echo "==================================="
echo ""

# 启动开发服务器
pnpm dev

