package com.svalero.music.rights.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

/**
 * Servicio de almacenamiento en AWS S3.
 *
 * Las credenciales se obtienen de la cadena de proveedores por defecto del SDK
 * (variables de entorno AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY o ~/.aws/credentials).
 * El bucket y la región se leen de application.properties (aws.s3.bucket / aws.s3.region).
 */
@Service
public class S3StorageService {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    private S3Client s3Client;
    private S3Presigner presigner;

    @PostConstruct
    public void init() {
        Region r = Region.of(region);
        this.s3Client = S3Client.builder().region(r).build();
        this.presigner = S3Presigner.builder().region(r).build();
    }

    @PreDestroy
    public void close() {
        if (s3Client != null) {
            s3Client.close();
        }
        if (presigner != null) {
            presigner.close();
        }
    }

    /** Borra el objeto de S3 con la key indicada. */
    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    /** Sube los bytes a S3 bajo la key indicada y devuelve esa key. */
    public String upload(String key, byte[] content, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(content));
        return key;
    }

    /**
     * Genera una URL prefirmada de descarga (GET), válida durante el TTL indicado.
     * Fuerza la descarga (Content-Disposition: attachment) con el nombre de archivo dado,
     * en vez de que el navegador abra el PDF en línea.
     */
    public String presignedGetUrl(String key, String filename, Duration ttl) {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType("application/pdf")
                .responseContentDisposition("attachment; filename=\"" + filename + "\"")
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(ttl)
                .getObjectRequest(getRequest)
                .build();
        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);
        return presigned.url().toString();
    }
}
