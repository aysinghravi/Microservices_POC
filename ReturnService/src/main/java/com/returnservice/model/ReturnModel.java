package com.returnservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "returnOrder")
public class ReturnModel {

    @Id
    private String returnOrderNumber;
    @NotNull(message = "Order number cannot be empty")
    private String orderNumber;
    @NotNull(message = "Customer name cannot be empty")
    private String customName;
    @NotNull(message = "Customer address cannot be empty")
    private String address;
    private String city;
    private String locality;
    private String state;
    @NotNull(message = "Customer email cannot be empty")
    private String email;
    private String zipcode;
    private String country;
    @NotNull(message = "Customer mobile number cannot be empty")
    private String mobile;

    private String status;
    private String source;
    @CreatedDate
    private Date returnON;


    private List<ReturnLineEntries> returnLineEntries;


    @Data
    public static class ReturnLineEntries {
        @NotNull(message = "Item id cannot be empty")
        private String itemId;
        private String itemDescription;
        @NotNull(message = "Quantity cannot be empty")
        private int quantity;
        @NotNull(message = "Reason cannot be empty")
        private String reason;
    }


}