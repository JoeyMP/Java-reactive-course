package com.react.project.webflux.sec08.dto;

import java.util.UUID;

public record UploadResponse(UUID confirmationId,
                             Long productCount) {
}
