# 🏥 Hospital Management System (HMS)

## Tech Stack

| Component | Version |
|---|---|
| Java | 1.8 |
| Spring Boot | 2.7.14 |
| Spring Security | 5.7.x |
| JWT (jjwt) | 0.11.5 |
| MySQL | 8.0 |
| Lombok | Latest |
| Maven | 3.9+ |

## Setup Instructions

### Step 1: Create MySQL Database
```sql
CREATE DATABASE hms_db;
```

### Step 2: Update Database Credentials
Edit `src/main/resources/application.properties` — change username/password if needed.

### Step 3: Import in STS
- File → Import → Existing Maven Project → Select HMS folder → Finish
- Wait for Maven to download all dependencies

### Step 4: Run
- Right-click project → Run As → Spring Boot App
- Server starts at `http://localhost:8080`

## API Reference (17 Endpoints)

### Auth APIs (Public)
| Method | Endpoint | Body |
|---|---|---|
| POST | `/api/v1/auth/register` | `{"username":"admin1","password":"admin123","role":"ADMIN"}` |
| POST | `/api/v1/auth/login` | `{"username":"admin1","password":"admin123"}` |

### Patient APIs (Secured)
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/patients` | ADMIN, RECEPTIONIST |
| GET | `/api/v1/patients` | ALL ROLES |
| GET | `/api/v1/patients/{id}` | ALL ROLES |
| PUT | `/api/v1/patients/{id}` | ADMIN, RECEPTIONIST |
| DELETE | `/api/v1/patients/{id}` | ADMIN |

**Body:** `{"name":"John","age":30,"gender":"Male","contact":"9876543210","address":"123 Main St"}`

### Doctor APIs (Secured)
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/doctors` | ADMIN |
| GET | `/api/v1/doctors` | ADMIN, RECEPTIONIST |
| GET | `/api/v1/doctors/{id}` | ADMIN, RECEPTIONIST |

**Body:** `{"name":"Dr. Smith","specialization":"Cardiology","contact":"9876543211"}`

### Appointment APIs (Secured)
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/appointments` | ADMIN, RECEPTIONIST |
| GET | `/api/v1/appointments` | ADMIN, RECEPTIONIST |
| GET | `/api/v1/appointments/{id}` | ALL ROLES |
| PUT | `/api/v1/appointments/{id}` | ADMIN, RECEPTIONIST |
| DELETE | `/api/v1/appointments/{id}` | ADMIN |

**Create:** `{"patientId":1,"doctorId":1,"appointmentDate":"2026-07-15T10:30:00"}`
**Update:** `{"status":"COMPLETED"}`

### Medical Record APIs (Secured)
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/records` | ADMIN, DOCTOR |
| GET | `/api/v1/records/patient/{id}` | ADMIN, DOCTOR |

**Body:** `{"patientId":1,"doctorId":1,"diagnosis":"Hypertension","prescription":"Amlodipine 5mg","notes":"Follow up in 2 weeks"}`

## Testing with Postman

1. **Register:** POST `/api/v1/auth/register`
2. **Login:** POST `/api/v1/auth/login` → Copy JWT token
3. **Add header:** `Authorization: Bearer <your-token>`
4. Test all secured endpoints
