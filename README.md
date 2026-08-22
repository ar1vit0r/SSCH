# SSCH — Hypothetical Computer Simulator

A complete toolchain for a hypothetical computer based on the architecture from Calingaert's *Assemblers and Loaders*. Implements the full compilation pipeline: assembler, linker, loader, and CPU simulator with a JavaFX GUI.

## Architecture

The simulator models a simple CPU with:

- **Registers**: Program counter (PC), stack pointer (SP), general-purpose registers
- **Memory**: Addressable memory with stack support
- **Instruction set**: 16 opcodes including arithmetic, logic, I/O, stack operations, and control flow
- **3-pass assembler**: Symbol resolution, label table generation, binary output

## Components

| Module | Package | Description |
|--------|---------|-------------|
| **Assembler** (Montador) | `com.ps.montador` | 3-pass assembler: first pass builds symbol table, second resolves references, third emits binary |
| **Linker** (Ligador) | `com.ps.ligador` | Links multiple object files, resolves external references |
| **Loader** (Carregador) | `com.ps.carregador` | Loads linked programs into VM memory |
| **Macro Processor** | `com.ps.macros` | Preprocessor for macro expansion before assembly |
| **VM / Executor** | `com.ps.executor` | CPU simulator: fetch-decode-execute cycle with instruction dispatch |
| **GUI** | `com.ps.ui` | JavaFX interface for loading, assembling, and running programs |

## Requirements

- Java 11+
- JavaFX 13+ (included via Maven)

## Build & Run

```bash
cd SSCH
mvn compile
mvn exec:java
```

Or open in NetBeans (original IDE) and run from the project.

## Video Demo

[Project presentation video](https://drive.google.com/drive/u/1/folders/1-F7YZlgiPFt_6ErNAe_FW2xA5I3Et7gt) (requires @inf login)

## Skills Demonstrated

- Computer architecture and instruction set design
- Multi-pass assembler implementation (symbol tables, relocation, binary emission)
- Linker/loader pipeline (external references, memory allocation)
- Macro processing and text substitution
- VM design with fetch-decode-execute cycle
- JavaFX GUI development
- Object-oriented design with clean package separation

## Authors

José Felipe, Ari Vítor, Ariam Barth, Frederico Antunes, Frederico Schawm, Gabriel Gomes, Lucas Moraes, Matheus Chaves.
