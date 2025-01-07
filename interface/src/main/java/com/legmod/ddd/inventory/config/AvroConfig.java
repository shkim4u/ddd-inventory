package com.legmod.ddd.inventory.config;

import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.MimeType;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class AvroConfig {
    @Bean
    public MessageConverter avroMessageConverter() {
        return new AvroMessageConverter();
    }

    public static class AvroMessageConverter extends AbstractMessageConverter {

        protected AvroMessageConverter() {
            super(new MimeType("application", "avro"));
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return SpecificRecord.class.isAssignableFrom(clazz);
        }

        @Override
        protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
            byte[] payload = (byte[]) message.getPayload();
            SpecificDatumReader<?> datumReader = new SpecificDatumReader<>(targetClass);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(payload)) {
                return datumReader.read(null, DecoderFactory.get().binaryDecoder(inputStream, null));
            } catch (IOException e) {
                throw new MessageConversionException("Failed to convert Avro message", e);
            }
        }
    }
}
