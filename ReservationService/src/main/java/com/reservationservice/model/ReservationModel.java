package com.reservationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Reservation")
public class ReservationModel {
    @Id
    private String reservationId;
    @NotNull(message = "Order Number Cannot be null")
    private String orderNumber;
    private String status;
    @CreatedDate
    private Date createAt;
    @LastModifiedDate
    private Date updatedAt;
    private List<itemList> itemList;
    @Data
    public static class itemList{
        private String itemId;
        @Size(max = 10,message = "Quantity cannot be greater than 10")
        private int quantity;
    }
}
