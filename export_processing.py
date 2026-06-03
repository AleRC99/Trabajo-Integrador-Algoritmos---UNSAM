#!/usr/bin/env python3
"""
export_processing.py
────────────────────────────────────────────────────────────
Genera la carpeta processing-export/Game1982/ lista para
abrir desde Processing IDE.

Estructura esperada del repo:
  repo/
  ├── contracts/       ← interfaces y contratos (ModuloJuego, etc.)
  ├── lobby/           ← clases del Home (HomeJuego, Gestores, UI...)
  ├── assets/
  │   └── fonts/
  │       └── PressStart2P-Regular.ttf
  ├── modulos/
  │   ├── skyhawk/
  │   │   ├── ModuloSkyhawk.java
  │   │   ├── SkyhawkJugador.java
  │   │   └── data/
  │   ├── pucara/
  │   │   └── ...
  │   └── ...
  ├── Game1982.pde
  └── processing-export/
      └── Game1982/    ← GENERADO por este script

Uso:
  python export_processing.py              # exporta todos los módulos
  python export_processing.py skyhawk     # exporta solo el módulo skyhawk
  python export_processing.py --dry-run   # muestra qué haría sin copiar nada
"""

import sys
import os
import shutil
import argparse
from pathlib import Path


REPO_ROOT     = Path(__file__).parent           # carpeta donde está el script
CONTRACTS_DIR = REPO_ROOT / "contracts"
LOBBY_DIR     = REPO_ROOT / "lobby"
MODULOS_DIR   = REPO_ROOT / "modulos"
ASSETS_DIR    = REPO_ROOT / "assets"
PDE_FILE      = REPO_ROOT / "Game1982.pde"
EXPORT_DIR    = REPO_ROOT / "processing-export" / "Game1982"

GREEN  = "\033[92m"
YELLOW = "\033[93m"
RED    = "\033[91m"
GRAY   = "\033[90m"
RESET  = "\033[0m"
BOLD   = "\033[1m"

def ok(msg):    print(f"  {GREEN}✓{RESET}  {msg}")
def warn(msg):  print(f"  {YELLOW}⚠{RESET}  {msg}")
def err(msg):   print(f"  {RED}✗{RESET}  {msg}")
def info(msg):  print(f"  {GRAY}→{RESET}  {msg}")
def head(msg):  print(f"\n{BOLD}{msg}{RESET}")


def copiar_java(src: Path, dest_dir: Path, dry_run: bool, etiqueta: str = ""):
    """Copia un .java a dest_dir. Si ya existe y es idéntico, lo indica."""
    dest = dest_dir / src.name
    if dest.exists() and dest.read_bytes() == src.read_bytes():
        info(f"  sin cambios   {src.name}  {GRAY}({etiqueta}){RESET}")
        return False
    if dry_run:
        info(f"  copiaría      {src.name}  {GRAY}({etiqueta}){RESET}")
        return True
    shutil.copy2(src, dest)
    ok(f"  {src.name}  {GRAY}({etiqueta}){RESET}")
    return True


def copiar_recursos(src_dir: Path, dest_dir: Path, dry_run: bool, etiqueta: str):
    """Copia recursos preservando la estructura interna de carpetas."""
    if not src_dir.exists():
        return
    dest_dir.mkdir(parents=True, exist_ok=True)
    archivos = list(src_dir.rglob("*"))
    assets = [f for f in archivos if f.is_file()]
    if not assets:
        return
    for asset in assets:
        relative = asset.relative_to(src_dir)
        dest = dest_dir / relative
        if dest.exists() and dest.read_bytes() == asset.read_bytes():
            info(f"  sin cambios   {dest_dir.name}/{relative}  {GRAY}({etiqueta}){RESET}")
            continue
        if dry_run:
            info(f"  copiaría      {dest_dir.name}/{relative}  {GRAY}({etiqueta}){RESET}")
            continue
        dest.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(asset, dest)
        ok(f"  {dest_dir.name}/{relative}  {GRAY}({etiqueta}){RESET}")


def detectar_conflictos(javas: list[Path]) -> list[str]:
    """Detecta nombres de archivo repetidos en la lista."""
    nombres = [f.name for f in javas]
    vistos = set()
    conflictos = []
    for n in nombres:
        if nombres.count(n) > 1 and n not in vistos:
            conflictos.append(n)
            vistos.add(n)
    return conflictos


def validar_estructura():
    """Verifica que existan las carpetas mínimas esperadas."""
    faltantes = []
    for d in [CONTRACTS_DIR, LOBBY_DIR]:
        if not d.exists():
            faltantes.append(str(d.relative_to(REPO_ROOT)))
    if not PDE_FILE.exists():
        faltantes.append("Game1982.pde")
    return faltantes


def listar_modulos():
    """Devuelve los módulos disponibles en modulos/."""
    if not MODULOS_DIR.exists():
        return []
    return [d.name for d in sorted(MODULOS_DIR.iterdir()) if d.is_dir()]


def exportar(modulos_filtro: list[str] | None, dry_run: bool):
    head("Verificando estructura del repositorio...")
    faltantes = validar_estructura()
    if faltantes:
        err(f"Carpetas/archivos faltantes: {', '.join(faltantes)}")
        err("Asegurate de correr el script desde la raíz del repo.")
        sys.exit(1)
    ok("Estructura válida")

    head(f"Preparando {EXPORT_DIR.relative_to(REPO_ROOT)}/...")
    if not dry_run:
        EXPORT_DIR.mkdir(parents=True, exist_ok=True)
        (EXPORT_DIR / "data").mkdir(exist_ok=True)
    else:
        info("(modo dry-run — no se modifica nada)")

    javas_contracts = sorted(CONTRACTS_DIR.glob("*.java"))
    javas_lobby     = sorted(LOBBY_DIR.glob("*.java"))

    modulos_disponibles = listar_modulos()
    if not modulos_disponibles:
        warn("No se encontraron módulos en modulos/")
    
    modulos_a_exportar = []
    for m in modulos_disponibles:
        if modulos_filtro is None or m in modulos_filtro:
            modulos_a_exportar.append(m)
    
    if modulos_filtro:
        no_encontrados = [m for m in modulos_filtro if m not in modulos_disponibles]
        for m in no_encontrados:
            warn(f"Módulo '{m}' no encontrado en modulos/ — ignorado")

    javas_modulos = []
    for m in modulos_a_exportar:
        javas_modulos += sorted((MODULOS_DIR / m).glob("*.java"))

    todos = javas_contracts + javas_lobby + javas_modulos
    conflictos = detectar_conflictos(todos)
    if conflictos:
        head("⚠  Conflictos de nombres detectados:")
        for c in conflictos:
            warn(f"  '{c}' aparece más de una vez — solo se copiará la última versión")

    head("Copiando Game1982.pde...")
    if not dry_run:
        shutil.copy2(PDE_FILE, EXPORT_DIR / "Game1982.pde")
    ok("Game1982.pde")

    head(f"Copiando contracts/ ({len(javas_contracts)} archivos)...")
    for java in javas_contracts:
        copiar_java(java, EXPORT_DIR, dry_run, "contracts")

    head(f"Copiando lobby/ ({len(javas_lobby)} archivos)...")
    for java in javas_lobby:
        copiar_java(java, EXPORT_DIR, dry_run, "lobby")

    if modulos_a_exportar:
        head(f"Copiando módulos: {', '.join(modulos_a_exportar)}")
        for m in modulos_a_exportar:
            modulo_dir = MODULOS_DIR / m
            javas = sorted(modulo_dir.glob("*.java"))
            if not javas:
                warn(f"  [{m}] no tiene archivos .java")
                continue
            for java in javas:
                copiar_java(java, EXPORT_DIR, dry_run, m)
            copiar_recursos(modulo_dir / "data", EXPORT_DIR / "data", dry_run, m)
    else:
        warn("No se exportó ningún módulo (no hay módulos o el filtro no coincidió)")

    if ASSETS_DIR.exists():
        head("Copiando assets/ raíz...")
        copiar_recursos(ASSETS_DIR, EXPORT_DIR / "assets", dry_run, "assets")

    lobby_data = LOBBY_DIR / "data"
    if lobby_data.exists():
        head("Copiando data/ del lobby...")
        copiar_recursos(lobby_data, EXPORT_DIR / "data", dry_run, "lobby")

    head("Resumen")
    if not dry_run:
        javas_en_export = list(EXPORT_DIR.glob("*.java"))
        assets_en_export = list((EXPORT_DIR / "data").rglob("*"))
        assets_en_export += list((EXPORT_DIR / "assets").rglob("*"))
        assets_en_export = [f for f in assets_en_export if f.is_file()]
        ok(f"{len(javas_en_export)} archivos .java en processing-export/Game1982/")
        ok(f"{len(assets_en_export)} assets en processing-export/Game1982/")
        print()
        print(f"  {BOLD}Abrir desde Processing IDE:{RESET}")
        print(f"  {GRAY}{EXPORT_DIR}/Game1982.pde{RESET}")
    else:
        info("Dry-run completado. Corré sin --dry-run para aplicar los cambios.")


def limpiar(dry_run: bool):
    head(f"Limpiando {EXPORT_DIR.relative_to(REPO_ROOT)}/...")
    if not EXPORT_DIR.exists():
        warn("La carpeta de exportación no existe, nada que limpiar.")
        return
    if dry_run:
        info("(dry-run) borraría " + str(EXPORT_DIR))
        return
    shutil.rmtree(EXPORT_DIR)
    EXPORT_DIR.mkdir(parents=True)
    (EXPORT_DIR / "data").mkdir()
    ok("Carpeta limpiada")


def main():
    parser = argparse.ArgumentParser(
        description="Exporta el proyecto a una carpeta compatible con Processing IDE.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Ejemplos:
  python export_processing.py                    # exporta todos los módulos
  python export_processing.py skyhawk pucara    # exporta solo esos módulos
  python export_processing.py --dry-run         # muestra qué haría sin copiar
  python export_processing.py --clean           # limpia la carpeta de exportación
  python export_processing.py --list            # lista los módulos disponibles
        """
    )
    parser.add_argument(
        "modulos", nargs="*",
        help="Módulos a exportar (por defecto: todos)"
    )
    parser.add_argument(
        "--dry-run", action="store_true",
        help="Muestra qué haría sin modificar nada"
    )
    parser.add_argument(
        "--clean", action="store_true",
        help="Limpia la carpeta de exportación antes de exportar"
    )
    parser.add_argument(
        "--list", action="store_true",
        help="Lista los módulos disponibles y sale"
    )

    args = parser.parse_args()

    print(f"\n{BOLD}export_processing.py — 1982: Conflicto del Atlántico Sur{RESET}")
    print(f"{GRAY}{'─' * 54}{RESET}")

    if args.list:
        modulos = listar_modulos()
        if modulos:
            head("Módulos disponibles en modulos/:")
            for m in modulos:
                javas = list((MODULOS_DIR / m).glob("*.java"))
                info(f"  {m}  {GRAY}({len(javas)} .java){RESET}")
        else:
            warn("No se encontraron módulos en modulos/")
        return

    if args.clean:
        limpiar(args.dry_run)

    filtro = args.modulos if args.modulos else None
    exportar(filtro, args.dry_run)

    print()


if __name__ == "__main__":
    main()
