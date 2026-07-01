package com.hms.entity;

/**
 * Roles for Role-Based Access Control (RBAC).
 * ADMIN        → Full access to all APIs
 * DOCTOR       → Patient records + medical records access
 * RECEPTIONIST → Patient registration + appointment management
 */
public enum Role {
    ADMIN,
    DOCTOR,
    RECEPTIONIST
}
