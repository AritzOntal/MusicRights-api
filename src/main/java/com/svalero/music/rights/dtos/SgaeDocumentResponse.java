package com.svalero.music.rights.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta al generar/descargar un documento SGAE: metadatos + URL de descarga prefirmada.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SgaeDocumentResponse {
    private Long id;
    private String filename;
    private Long size;
    private float completionPercentage;
    private boolean complete;
    private String downloadUrl;
}
