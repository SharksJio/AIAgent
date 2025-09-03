# GitHub Actions Workflows

This repository includes GitHub Actions workflows for automated Android builds.

## Available Workflows

### 1. Android Build (`android-build.yml`)
- **Triggers**: Push/PR to main/master branches, manual dispatch
- **Purpose**: Simple Android build with debug and release APKs
- **Outputs**: 
  - Debug APK (`app-debug-apk`)
  - Release APK (`app-release-apk`)
  - Test results

### 2. Android Build and Test (`android-build-comprehensive.yml`)
- **Triggers**: Push/PR to main/master/develop branches, manual dispatch
- **Purpose**: Comprehensive build with matrix testing across multiple API levels
- **Features**:
  - Matrix builds for API levels 29 and 34
  - Lint checking
  - Unit tests
  - Instrumented tests (on macOS with emulator)
  - Configurable build types via workflow dispatch
- **Outputs**:
  - Debug/Release APKs for each API level
  - Lint reports
  - Test results

## Manual Workflow Dispatch

You can manually trigger the comprehensive workflow with different build types:
- `debug`: Build only debug APK
- `release`: Build only release APK  
- `both`: Build both debug and release APKs

## Artifacts

All workflows upload build artifacts that can be downloaded from the Actions tab:
- **APK files**: Available for 30 days
- **Test results**: Available for 7 days
- **Lint reports**: Available for 7 days

## Requirements

The workflows automatically set up:
- JDK 17 (Temurin distribution)
- Android SDK with appropriate API levels
- Gradle caching for faster builds

## APK Installation

The generated APKs are testable and can be installed on Android devices:
1. Download the APK artifact from the workflow run
2. Enable "Install from unknown sources" on your Android device
3. Transfer and install the APK file

## Build Configuration

The project is configured with:
- **Compile SDK**: 34
- **Target SDK**: 34  
- **Minimum SDK**: 24
- **Gradle**: 8.3
- **Android Gradle Plugin**: 8.1.0
- **Kotlin**: 1.9.0