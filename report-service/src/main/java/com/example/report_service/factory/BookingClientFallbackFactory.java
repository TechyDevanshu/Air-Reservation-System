package com.example.report_service.factory;

import com.example.report_service.Client.BookingClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingClientFallbackFactory implements FallbackFactory<BookingClient> {

    @Override
    public BookingClient create(Throwable cause) {
        // graceful degradation
        return List::of;
    }
}
