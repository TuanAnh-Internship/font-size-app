
# deploy.ps1 - Build, install va test tren dien thoai that
# Cach dung: .\deploy.ps1
# Chay tu thu muc: font-size-app\source\

# Tu dong lay thiet bi dang ket noi qua adb
$devices = @(adb devices | Where-Object { $_ -match '\s+device$' })
if ($devices.Count -eq 0) {
    Write-Host "KHONG TIM THAY THIET BI ADB NAO DANG KET NOI!" -ForegroundColor Red
    Write-Host "Hay kiem tra adb connect hoac cam cap USB." -ForegroundColor Yellow
    exit 1
}
$DEVICE = ($devices[0].Trim() -split '\s+')[0]
Write-Host ">>> Phat hien thiet bi: $DEVICE" -ForegroundColor Yellow

$APK    = ".\app\build\outputs\apk\debug\app-debug.apk"
$PKG    = "com.example.fontsizecontroller"
$TAG    = "FontSizeTest"

Write-Host ""
Write-Host ">>> STEP 1: Building APK..." -ForegroundColor Cyan
.\gradlew.bat assembleDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "BUILD FAILED - Dung lai." -ForegroundColor Red
    exit 1
}
Write-Host "Build OK!" -ForegroundColor Green

Write-Host ""
Write-Host ">>> STEP 2: Installing on device..." -ForegroundColor Cyan
adb -s $DEVICE install -r $APK
if ($LASTEXITCODE -ne 0) {
    Write-Host "INSTALL FAILED - Kiem tra ket noi ADB." -ForegroundColor Red
    exit 1
}
Write-Host "Install OK!" -ForegroundColor Green

Write-Host ""
Write-Host ">>> STEP 3: Launching app..." -ForegroundColor Cyan
adb -s $DEVICE shell am start -n "$PKG/.MainActivity"
Start-Sleep -Seconds 2

Write-Host ""
Write-Host ">>> STEP 4: Logcat output (tag: $TAG)" -ForegroundColor Cyan
Write-Host "--------------------------------------------" -ForegroundColor DarkGray
adb -s $DEVICE logcat -s $TAG -d
Write-Host "--------------------------------------------" -ForegroundColor DarkGray
Write-Host "Xem tren dien thoai roi kiem tra log o tren!" -ForegroundColor Green
