#!/bin/bash

# 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

echo "==================================="
echo "启动后端应用 - Notebook LLM"
echo "==================================="

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "错误: Java 未安装"
    echo "请先安装 JDK 17 或更高版本"
    exit 1
fi

# 检查Java版本
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "错误: Java 版本过低"
    echo "需要 JDK 17 或更高版本，当前版本: $JAVA_VERSION"
    exit 1
fi

echo "Java 版本检查通过: $(java -version 2>&1 | head -n 1)"

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: Maven 未安装"
    echo "请先安装 Maven 3.8 或更高版本"
    exit 1
fi

echo "Maven 版本: $(mvn -version | head -n 1 | awk '{print $3}')"

# 检查数据库连接（可选）
echo "检查数据库连接..."
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_USER=${DB_USER:-postgres}
DB_NAME=${DB_NAME:-notebook_llm}

# 检查PostgreSQL是否可用
if command -v psql &> /dev/null; then
    export PGPASSWORD=${DB_PASSWORD:-password}
    if psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT 1;" &> /dev/null; then
        echo "数据库连接检查通过"
    else
        echo "警告: 无法连接到数据库 $DB_NAME"
        echo "请确保:"
        echo "  1. PostgreSQL 服务已启动"
        echo "  2. 数据库 $DB_NAME 已创建"
        echo "  3. 数据库连接信息正确（可在 application.yml 中配置）"
        echo ""
        read -p "是否继续启动应用? (y/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
    unset PGPASSWORD
else
    echo "警告: psql 未安装，跳过数据库连接检查"
fi

echo ""
echo "正在编译项目..."
echo "==================================="

# 编译项目（如果必要）
if [ ! -d "target" ] || [ ! -f "target/notebook-llm-1.0.0.jar" ]; then
    echo "检测到项目未编译，正在编译..."
    mvn clean package -DskipTests
    if [ $? -ne 0 ]; then
        echo "错误: 项目编译失败"
        exit 1
    fi
    echo "编译完成"
else
    echo "项目已编译，跳过编译步骤"
fi

echo ""
echo "==================================="
echo "正在启动应用..."
echo "应用地址: http://localhost:8080"
echo "按 Ctrl+C 停止应用"
echo "==================================="
echo ""

# 启动应用
if [ -f "target/notebook-llm-1.0.0.jar" ]; then
    # 使用jar包启动
    java -jar target/notebook-llm-1.0.0.jar
else
    # 使用Maven启动
    mvn spring-boot:run
fi

