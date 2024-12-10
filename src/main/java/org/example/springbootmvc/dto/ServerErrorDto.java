package org.example.springbootmvc.dto;

import java.time.LocalDateTime;

public record ServerErrorDto(String message,
                            String detailedMessage,
                            LocalDateTime dateTime) {
}
