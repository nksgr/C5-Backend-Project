package com.sweethome.paymentservice.Controller;

import com.sweethome.paymentservice.Converter;
import com.sweethome.paymentservice.dto.TransactionDTO;
import com.sweethome.paymentservice.entity.TransactionDetailsEntity;
import com.sweethome.paymentservice.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    // paymentService instance is used to invoke payment related methods
    @Autowired
    private PaymentService paymentService ;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping (value="/test")
    public ResponseEntity helloPayment(){
        return new ResponseEntity("The payment controller received your request", HttpStatus.OK);
    }

    // POST API to create transaction and store into Database
    // It will return transaction/payment detail data as a response
    @PostMapping(value="/transaction", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTransaction(@RequestBody TransactionDTO transactionDTO){

        TransactionDetailsEntity transaction = modelMapper.map(transactionDTO, TransactionDetailsEntity.class);
        TransactionDetailsEntity createtransaction = paymentService.makeTransaction(transaction);
        TransactionDTO savedtransactionDTO = modelMapper.map(createtransaction, TransactionDTO.class);

        int transactionId = savedtransactionDTO.getTransactionId();
        System.out.println(transactionId);
        return new ResponseEntity(transactionId, HttpStatus.CREATED);

    }

    // GET API to get payment/transaction detail based on id
    @GetMapping(value = "/transaction/{transactionId}")
    public ResponseEntity getUser(@PathVariable(name = "transactionId")  int transactionId){

        TransactionDetailsEntity transaction = paymentService.getTransactionBasedOnId(transactionId);
        TransactionDTO transactionDTO = Converter.covertPaymentEntityToDTO(transaction);
        return new ResponseEntity(transactionDTO, HttpStatus.OK);

    }

}
