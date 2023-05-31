package com.sweethome.BookingService.service;

import com.sweethome.BookingService.DTO.PaymentDTO;
import com.sweethome.BookingService.entities.BookingInfoEntity;
import com.sweethome.BookingService.DTO.BookingDTO;

import java.util.ArrayList;
import java.util.List;

public interface BookingService {
    public BookingInfoEntity saveBookingDetails(BookingDTO bookingInfo);

    BookingInfoEntity updateBookingDetails(BookingInfoEntity updatedBooking);

    public BookingInfoEntity getBookingDetailsById(int id);

    public List<BookingInfoEntity> getAllBookings();

    public int calculatePrice(int noOfRooms, int noOfDays);

    public ArrayList<String> getRoomNumbers(int noOfRooms);


    BookingInfoEntity confirmPaymentForBooking(int bookingId, PaymentDTO paymentDTO);
}
