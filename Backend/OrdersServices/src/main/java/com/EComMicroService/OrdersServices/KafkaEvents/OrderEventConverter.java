package com.EComMicroService.OrdersServices.KafkaEvents;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

public class OrderEventConverter implements AttributeConverter<Events, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Events attribute) {
        try {
            return attribute == null ? null : mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to convert OrderEvent to JSON", e);
        }
    }

    @Override
    public Events convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : mapper.readValue(dbData, Events.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read OrderEvent from JSON", e);
        }
    }
}