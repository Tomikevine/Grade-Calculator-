# Download and setup Maven locally
# Run this in PowerShell in the project directory

$mavenVersion = "3.9.6"
$downloadUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$extractPath = "$PSScriptRoot\tools"
$mavenPath = "$extractPath\apache-maven-$mavenVersion"

Write-Host "Downloading Maven $mavenVersion..."
Write-Host "This may take a minute..."

# Create tools directory
New-Item -ItemType Directory -Path $extractPath -Force | Out-Null

# Download Maven
$ProgressPreference = 'SilentlyContinue'
Invoke-WebRequest -Uri $downloadUrl -OutFile "$extractPath\maven.zip" -UseBasicParsing

Write-Host "Extracting Maven..."
Expand-Archive -Path "$extractPath\maven.zip" -DestinationPath $extractPath -Force

# Remove zip file
Remove-Item -Path "$extractPath\maven.zip" -Force

# Add Maven to PATH for current session
$env:Path = "$mavenPath\bin;$env:Path"

Write-Host "Maven setup complete!"
Write-Host "Maven version:"
mvn --version

Write-Host ""
Write-Host "Running Grade Calculator..."
Write-Host ""

& mvn clean compile "exec:java" "-Dexec.mainClass=com.example.gradcalc.console.ConsoleAppKt" "-Dexec.cleanupDaemonThreads=false"
