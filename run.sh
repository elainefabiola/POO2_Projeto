#!/bin/sh

# Script para compilar e executar o Sistema ADA LocateCar
# Requer Java OpenJDK 21

echo "🚗 Sistema ADA LocateCar"
echo "========================"

# Verifica se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Erro: Java não encontrado. Instale Java OpenJDK 21."
    exit 1
fi

# Verifica se javac está instalado
if ! command -v javac &> /dev/null; then
    echo "❌ Erro: javac não encontrado. Instale Java OpenJDK 21 JDK."
    exit 1
fi

# Verifica a versão do Java
java_version=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$java_version" -lt 21 ]; then
    echo "⚠️  Aviso: Java OpenJDK 21 recomendado. Versão atual: $(java -version 2>&1 | head -1)"
fi

echo "🔧 Compilando o projeto..."

# Remove arquivos .class antigos
find . -name "*.class" -delete

# Compila todos os arquivos Java (incluindo Main.java na raiz)
javac -cp . -d . Main.java src/**/*.java

# Verifica se a compilação foi bem-sucedida
if [ $? -eq 0 ]; then
    echo "✅ Compilação concluída com sucesso!"
    echo "🚀 Executando o programa..."
    echo ""
    java -cp . Main
else
    echo "❌ Erro na compilação. Verifique o código fonte."
    exit 1
fi