# Smart Tourist Agent - Auto Bootstrap & Run Script
# This script will check for Maven locally, download a portable Maven if missing, and run the Spring Boot server.

$ErrorActionPreference = "Stop"

# Define portable maven directory
$PortableDir = "$PSScriptRoot\.maven-portable"
$MavenVersion = "3.9.6"
$MavenHome = "$PortableDir\apache-maven-$MavenVersion"
$MvnCmd = "$MavenHome\bin\mvn.cmd"

# Check if portable maven is already installed
if (-not (Test-Path $MvnCmd)) {
    Write-Host "--------------------------------------------------------" -ForegroundColor Cyan
    Write-Host "Apache Maven not detected. Initiating portable bootstrap..." -ForegroundColor Cyan
    Write-Host "--------------------------------------------------------" -ForegroundColor Cyan

    # Create destination directory
    if (-not (Test-Path $PortableDir)) {
        New-Item -ItemType Directory -Path $PortableDir | Out-Null
    }

    $ZipPath = "$PortableDir\maven.zip"
    $DownloadUrl = "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/apache-maven-$MavenVersion-bin.zip"

    Write-Host "Downloading Apache Maven $MavenVersion from Apache archives..." -ForegroundColor Yellow
    Invoke-WebRequest -Uri $DownloadUrl -OutFile $ZipPath -UserAgent "Mozilla/5.0"

    Write-Host "Extracting Maven package..." -ForegroundColor Yellow
    Expand-Archive -Path $ZipPath -DestinationPath $PortableDir -Force

    Write-Host "Cleaning temporary archive files..." -ForegroundColor Yellow
    Remove-Item -Path $ZipPath -Force

    Write-Host "Maven bootstrap completed successfully!" -ForegroundColor Green
}

# Add Maven bin directory to process-level PATH
$env:PATH = "$MavenHome\bin;" + $env:PATH
Write-Host "Environment set. Starting compilation and launching Smart Tourist Agent..." -ForegroundColor Green
Write-Host "Using Java version:" -ForegroundColor Gray
java -version

Write-Host "Running: mvn clean spring-boot:run" -ForegroundColor Cyan
# Run the application
& $MvnCmd clean spring-boot:run
