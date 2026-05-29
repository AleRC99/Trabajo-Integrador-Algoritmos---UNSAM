#!/bin/bash
# ============================================
# 1982 - Build and Run (Linux / macOS)
# ============================================
# Requiere: Java 8+ instalado (java y javac en PATH)
# Uso:      ./run.sh

set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src/main/java"
LIB_DIR="$PROJECT_DIR/lib"
OUT_DIR="$PROJECT_DIR/out"
RESOURCES_DIR="$PROJECT_DIR/src/main/Resources"

# Separador de classpath (: en Linux/Mac)
CP_SEP=":"

echo "=== 1982 - Compilando... ==="

# Limpiar y crear directorio de salida
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# Copiar recursos al directorio de salida
cp -r "$RESOURCES_DIR"/* "$OUT_DIR/" 2>/dev/null || true

# Compilar todos los .java
find "$SRC_DIR" -name "*.java" > "$OUT_DIR/sources.txt"
javac -cp "$LIB_DIR/core.jar" -d "$OUT_DIR" @"$OUT_DIR/sources.txt"
rm "$OUT_DIR/sources.txt"

echo "=== Compilacion exitosa ==="
echo "=== Ejecutando 1982... ==="

# Ejecutar
cd "$OUT_DIR"
java -cp ".$CP_SEP$LIB_DIR/core.jar" Home.GameApp
