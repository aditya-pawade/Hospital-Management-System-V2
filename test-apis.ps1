# =============================================
# HMS Full API Test Suite (17 Endpoints)
# =============================================

$base = "http://localhost:8080/api/v1"
$pass = 0
$fail = 0

function Test-Api {
    param($Name, $Method, $Uri, $Body, $Token, $ExpectedStatus)
    
    $headers = @{ "Content-Type" = "application/json" }
    if ($Token) { $headers["Authorization"] = "Bearer $Token" }
    
    try {
        $params = @{
            Uri = $Uri
            Method = $Method
            Headers = $headers
            UseBasicParsing = $true
        }
        if ($Body) { $params["Body"] = $Body }
        
        $response = Invoke-WebRequest @params
        $statusCode = $response.StatusCode
        $content = $response.Content | ConvertFrom-Json
        
        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "[PASS] $Name - Status: $statusCode" -ForegroundColor Green
            $script:pass++
        } else {
            Write-Host "[FAIL] $Name - Expected: $ExpectedStatus, Got: $statusCode" -ForegroundColor Red
            $script:fail++
        }
        return $content
    } catch {
        $statusCode = [int]$_.Exception.Response.StatusCode
        if ($statusCode -eq $ExpectedStatus) {
            Write-Host "[PASS] $Name - Status: $statusCode (expected error)" -ForegroundColor Green
            $script:pass++
        } else {
            Write-Host "[FAIL] $Name - Expected: $ExpectedStatus, Got: $statusCode" -ForegroundColor Red
            Write-Host "       Error: $_" -ForegroundColor Yellow
            $script:fail++
        }
        return $null
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  HMS API Test Suite - 17 Endpoints" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# Wait for tables to be recreated after DB reset
Start-Sleep -Seconds 3

# ---- AUTH APIs (Public) ----
Write-Host "--- AUTH APIs ---" -ForegroundColor Yellow

# 1. Register ADMIN
$r = Test-Api "1. POST /auth/register (ADMIN)" "POST" "$base/auth/register" '{"username":"admin1","password":"admin123","role":"ADMIN"}' $null 201

# 2. Register DOCTOR
$r = Test-Api "2. POST /auth/register (DOCTOR)" "POST" "$base/auth/register" '{"username":"doctor1","password":"doctor123","role":"DOCTOR"}' $null 201

# 3. Register RECEPTIONIST
$r = Test-Api "3. POST /auth/register (RECEPT)" "POST" "$base/auth/register" '{"username":"recep1","password":"recep123","role":"RECEPTIONIST"}' $null 201

# 4. Login as ADMIN
$loginResult = Invoke-RestMethod -Uri "$base/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"admin1","password":"admin123"}'
$adminToken = $loginResult.data.token
if ($adminToken) {
    Write-Host "[PASS] 4. POST /auth/login (ADMIN) - Token received" -ForegroundColor Green
    $pass++
} else {
    Write-Host "[FAIL] 4. POST /auth/login (ADMIN) - No token" -ForegroundColor Red
    $fail++
}

# 5. Login as DOCTOR
$loginResult = Invoke-RestMethod -Uri "$base/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"doctor1","password":"doctor123"}'
$doctorToken = $loginResult.data.token
if ($doctorToken) {
    Write-Host "[PASS] 5. POST /auth/login (DOCTOR) - Token received" -ForegroundColor Green
    $pass++
} else {
    Write-Host "[FAIL] 5. POST /auth/login (DOCTOR) - No token" -ForegroundColor Red
    $fail++
}

# ---- PATIENT APIs ----
Write-Host "`n--- PATIENT APIs ---" -ForegroundColor Yellow

# 6. Create Patient (ADMIN)
$r = Test-Api "6. POST /patients (create)" "POST" "$base/patients" '{"name":"John Doe","age":30,"gender":"Male","contact":"9876543210","address":"123 Main St"}' $adminToken 201

# 7. Get All Patients
$r = Test-Api "7. GET /patients (list)" "GET" "$base/patients" $null $adminToken 200

# 8. Get Patient by ID
$r = Test-Api "8. GET /patients/1 (by id)" "GET" "$base/patients/1" $null $adminToken 200

# 9. Update Patient
$r = Test-Api "9. PUT /patients/1 (update)" "PUT" "$base/patients/1" '{"name":"John Doe Updated","age":31,"gender":"Male","contact":"9876543210","address":"456 Oak Ave"}' $adminToken 200

# 10. Delete Patient (will create a second one to delete)
Test-Api "10a. POST /patients (create 2nd)" "POST" "$base/patients" '{"name":"Jane Smith","age":25,"gender":"Female","contact":"1234567890","address":"789 Elm St"}' $adminToken 201 | Out-Null
$r = Test-Api "10. DELETE /patients/2 (delete)" "DELETE" "$base/patients/2" $null $adminToken 200

# ---- DOCTOR APIs ----
Write-Host "`n--- DOCTOR APIs ---" -ForegroundColor Yellow

# 11. Create Doctor (ADMIN only)
$r = Test-Api "11. POST /doctors (create)" "POST" "$base/doctors" '{"name":"Dr. Smith","specialization":"Cardiology","contact":"9876543211"}' $adminToken 201

# 12. Get All Doctors
$r = Test-Api "12. GET /doctors (list)" "GET" "$base/doctors" $null $adminToken 200

# 13. Get Doctor by ID
$r = Test-Api "13. GET /doctors/1 (by id)" "GET" "$base/doctors/1" $null $adminToken 200

# ---- APPOINTMENT APIs ----
Write-Host "`n--- APPOINTMENT APIs ---" -ForegroundColor Yellow

# 14. Create Appointment
$r = Test-Api "14. POST /appointments (book)" "POST" "$base/appointments" '{"patientId":1,"doctorId":1,"appointmentDate":"2026-12-15T10:30:00"}' $adminToken 201

# 15. Get All Appointments
$r = Test-Api "15. GET /appointments (list)" "GET" "$base/appointments" $null $adminToken 200

# 16. Get Appointment by ID
$r = Test-Api "16. GET /appointments/1 (by id)" "GET" "$base/appointments/1" $null $adminToken 200

# 17. Update Appointment Status
$r = Test-Api "17. PUT /appointments/1 (status)" "PUT" "$base/appointments/1" '{"status":"COMPLETED"}' $adminToken 200

# 18. Delete Appointment (create 2nd first)
Test-Api "18a. POST /appointments (book 2nd)" "POST" "$base/appointments" '{"patientId":1,"doctorId":1,"appointmentDate":"2026-12-20T14:00:00"}' $adminToken 201 | Out-Null
$r = Test-Api "18. DELETE /appointments/2 (delete)" "DELETE" "$base/appointments/2" $null $adminToken 200

# ---- MEDICAL RECORD APIs ----
Write-Host "`n--- MEDICAL RECORD APIs ---" -ForegroundColor Yellow

# 19. Create Medical Record (ADMIN)
$r = Test-Api "19. POST /records (create)" "POST" "$base/records" '{"patientId":1,"doctorId":1,"diagnosis":"Hypertension","prescription":"Amlodipine 5mg","notes":"Follow up in 2 weeks"}' $adminToken 201

# 20. Get Records by Patient ID
$r = Test-Api "20. GET /records/patient/1" "GET" "$base/records/patient/1" $null $adminToken 200

# ---- RBAC TESTS ----
Write-Host "`n--- RBAC TESTS (Access Denied) ---" -ForegroundColor Yellow

# 21. DOCTOR cannot create patient (should be 403)
$r = Test-Api "21. DOCTOR POST /patients (403)" "POST" "$base/patients" '{"name":"Test","age":20,"gender":"Male","contact":"000","address":"none"}' $doctorToken 403

# 22. DOCTOR cannot create doctor (should be 403)
$r = Test-Api "22. DOCTOR POST /doctors (403)" "POST" "$base/doctors" '{"name":"Test Doc","specialization":"Test","contact":"000"}' $doctorToken 403

# ---- SUMMARY ----
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  RESULTS: $pass PASSED, $fail FAILED" -ForegroundColor $(if($fail -eq 0){"Green"}else{"Red"})
Write-Host "========================================`n" -ForegroundColor Cyan
