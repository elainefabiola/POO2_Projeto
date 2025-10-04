#!/bin/bash

# Script para compilar o projeto ADA LocateCar

echo "=========================================="
echo "Compilando projeto ADA LocateCar..."
echo "=========================================="
echo ""

# Criar diretório bin se não existir
mkdir -p bin

# Compilar todos os arquivos Java
javac -d bin -sourcepath . \
    Main.java \
    src/model/*.java \
    src/repositories/*.java \
    src/services/*.java \
    src/utils/*.java \
    src/views/*.java \
    src/functional/*.java

# Verificar se compilou com sucesso
if [ $? -eq 0 ]; then
    echo "✅ Compilação concluída com sucesso!"
    echo ""
    echo "Para executar o projeto, use:"
    echo "  java -cp bin Main"
    echo ""
else
    echo "❌ Erro na compilação!"
    exit 1
fi
