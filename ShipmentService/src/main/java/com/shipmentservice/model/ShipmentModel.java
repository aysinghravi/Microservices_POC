package com.shipmentservice.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Shipment")
@Schema(
        description = "Shipment Model Information"
)
public class ShipmentModel {

    @Id
   @Schema(description = "Shipment Number")
    private String shipmentNumber;
    @NotNull(message = "Customer name cannot be empty")
    @Schema(description = "Customer Name")
    private String custName;
    @NotNull(message = "Customer address cannot be empty")
    @Schema(description = "Customer Address")
    private String address;
    @Schema(description = "Cutomer City")
    private String city;
    @Schema(description = "Cutomer locality")
    private String locality;
    @Schema(description = "Cutomer State")
    private String state;
    @Schema(description = "Cutomer Email")
    @NotNull(message = "Customer email cannot be empty")
    @Email(message = "Valid email should be provided")
    private String email;
    @Schema(description = "Cutomer Zipcode")
    private String zipcode;
    @Schema(description = "Cutomer Country")
    private String country;
    @NotNull(message = "Customer mobile number cannot be empty")
    @Schema(description = "Cutomer Mobile Number")
    private String mobile;
    @NotNull(message = "order number cannot be empty")
    @Schema(description = "Order Number")
    private String orderNumber;
    //@NotNull(message = "source cannot be empty")
    @Schema(description = "Order Source")
    private String source;
    @Schema(description = "Order Status")
    private String status;

    @Schema(description = "Shipment shipping date")
    @CreatedDate
    private Date shippedOn;
    @Schema(description = "Shipment Updated On")
    @LastModifiedDate
    private Date updatedShipmentOn;

    @Data
    private static class ShipmentLineEntries{
        @NotNull(message = "Item id cannot be empty")
        @Schema(description = "Item Id")
        private String itemId;
        @Schema(description = "Description of Item")
        private String itemDescription;
        @NotNull(message = "Quantity cannot be empty")
        @Schema(description = "No of Items")
        private int quantity;
    }
    private List<ShipmentLineEntries> shipmentLineEntries;
}
