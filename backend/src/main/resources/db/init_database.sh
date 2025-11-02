#!/bin/bash

# 数据库初始化脚本
# 用于创建notebook_llm数据库

# 配置数据库连接信息（可根据实际情况修改）
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_USER=${DB_USER:-postgres}
DB_PASSWORD=${DB_PASSWORD:-password}
DB_NAME="notebook_llm"

echo "==================================="
echo "初始化数据库: $DB_NAME"
echo "==================================="

# 使用PGPASSWORD环境变量设置密码（避免交互式输入）
export PGPASSWORD=$DB_PASSWORD

# 检查PostgreSQL是否可用
if ! command -v psql &> /dev/null; then
    echo "错误: psql 未安装"
    echo "请先安装PostgreSQL客户端"
    exit 1
fi

# 检查数据库是否已存在
echo "检查数据库是否存在..."
DB_EXISTS=$(psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$DB_NAME'")

if [ "$DB_EXISTS" = "1" ]; then
    echo "数据库 $DB_NAME 已存在"
else
    echo "创建数据库 $DB_NAME..."
    psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d postgres -c "CREATE DATABASE $DB_NAME;"
    
    if [ $? -eq 0 ]; then
        echo "数据库 $DB_NAME 创建成功"
    else
        echo "错误: 数据库创建失败"
        exit 1
    fi
fi

# 执行初始化SQL
echo "执行初始化SQL..."
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$(dirname "$0")/init.sql"

if [ $? -eq 0 ]; then
    echo "数据库初始化完成"
    echo "==================================="
else
    echo "错误: 数据库初始化失败"
    exit 1
fi

# 清除密码环境变量
unset PGPASSWORD

