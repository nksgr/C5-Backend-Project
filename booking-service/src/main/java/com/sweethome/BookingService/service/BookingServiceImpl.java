package com.sweethome.BookingService.service;

import com.sweethome.BookingService.DTO.PaymentDTO;
import com.sweethome.BookingService.entities.BookingInfoEntity;
import com.sweethome.BookingService.DTO.BookingDTO;
import com.sweethome.BookingService.DAO.BookingDAO;
import com.sweethome.BookingService.exception.InvalidBooking;
import com.sweethome.BookingService.feign.PaymentClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BookingDAO bookingDAO;

    @Autowired
    PaymentClient paymentClient;


    @Override
    public BookingInfoEntity saveBookingDetails(BookingDTO bookingInfo) {

        // gets the room numbers list
        // calculate total price
        // then we convert the dto to entity
        // save the entity to db

        ArrayList<String> bookedRooms = getRoomNumbers(bookingInfo.getNumOfRooms());
        String roomNumbers = String.join(", ", bookedRooms);
        bookingInfo.setRoomNumbers(roomNumbers);

        long noOfDays = ChronoUnit.DAYS.between(bookingInfo.getFromDate(), bookingInfo.getToDate());
        int roomPrice = calculatePrice(bookingInfo.getNumOfRooms(), (int) noOfDays);
        bookingInfo.setRoomPrice(roomPrice);


        System.out.println(bookingInfo.toString());

        BookingInfoEntity bookingInfoEntityDetails = modelMapper.map(bookingInfo, BookingInfoEntity.class);

        System.out.println(bookingInfoEntityDetails.toString());

       return bookingDAO.save(bookingInfoEntityDetails);






    }

    @Override
    public BookingInfoEntity updateBookingDetails(BookingInfoEntity updatedBooking){
        return bookingDAO.save(updatedBooking);
    }


    @Override
    public BookingInfoEntity getBookingDetailsById(int id) {

        if (bookingDAO.findById(id).isPresent()){

            return bookingDAO.findById(id).get();
        }

        throw new InvalidBooking( "Invalid Payment Id", 400 );


    }

    @Override
    public List<BookingInfoEntity> getAllBookings() {
        return null;
    }

    @Override
    public int calculatePrice(int noOfRooms, int noOfDays) {
        return 1000 * noOfRooms * noOfDays;
    }

    @Override
    public ArrayList<String> getRoomNumbers(int noOfRooms) {
        Random random = new Random();
        int upperBound = 100;

        ArrayList<String> roomsList = new ArrayList<String>();

        for (int i= 0; i<noOfRooms; i++){
            roomsList.add(String.valueOf((random.nextInt(upperBound))));
        }



        return roomsList;
    }

    @Override
    public BookingInfoEntity confirmPaymentForBooking(int bookingId, PaymentDTO paymentDTO) {

        BookingInfoEntity bookingDetails = getBookingDetailsById(bookingId);

        paymentDTO.setBookingId(bookingId);

        System.out.println(bookingDetails.toString());

        int paymentConfirmed = paymentClient.getPaymentConfirmation(paymentDTO);

        System.out.println(paymentConfirmed);



        bookingDetails.setTransactionId(paymentConfirmed);

        bookingDetails.setBookedOn(getCurrentDateTime());


        return updateBookingDetails(bookingDetails);
    }

    public LocalDateTime getCurrentDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now;
    }
}
