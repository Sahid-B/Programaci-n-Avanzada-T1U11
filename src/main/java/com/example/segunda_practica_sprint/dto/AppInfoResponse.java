// Declaración del paquete donde se ubican los objetos de transferencia de datos (Data Transfer Objects)
package com.example.segunda_practica_sprint.dto;

/**
 * Record inmutable que modela el DTO de respuesta JSON expuesto por el endpoint /api/info.
 * En Java, un 'record' genera automáticamente constructor canónico, getters, equals, hashCode y toString.
 */
public record AppInfoResponse(
        // Nombre identificador de la aplicación
        String name,

        // Breve descripción o propósito de la aplicación
        String description,

        // Versión actual del proyecto, sincronizada dinámicamente desde el pom.xml
        String version,

        // Nombre del desarrollador o autor responsable
        String developer,

        // Correo electrónico institucional o de contacto del desarrollador
        String email,

        // Rol o cargo del autor dentro del desarrollo (ej. Developer)
        String role,

        // Entorno de ejecución activo obtenido del perfil (ej. dev, prod, test)
        String environment
) {}
