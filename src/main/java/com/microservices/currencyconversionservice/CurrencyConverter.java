package com.microservices.currencyconversionservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConverter {

    private Long id;

    private String from;

    private String to;

    private BigDecimal conversionMultiple;

    private BigDecimal quantity;

    private BigDecimal totalAmount;

    private int port;
}
