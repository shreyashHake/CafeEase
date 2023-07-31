package com.cafe.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "bill")
public class Bill implements Serializable {
    private static final long versionSerialUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uuid;
    private String name;
    private String email;
    @Column(name = "contactnumber")
    private String contactNumber;
    @Column(name = "paymentmethod")
    private String paymentMethod;
    private Integer total;
//    @Column(name = "productdetails", columnDefinition = "json")
    @Column(name = "productdetails", columnDefinition = "json")
    private String productDetails;
    @Column(name = "cretedby")
    private String createdBy;

}
