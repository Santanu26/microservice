package com.microservices.currencyconversionservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CurrencyConversionController {


    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);
    private static final String INVOKE_URL = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";
    private static final String URL_TEMP = "localhost:8100/currency-conversion/from/USD/to/BDT/quantity/100";

    private final CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConverter getCurrentConverter(@PathVariable String from,
                                                 @PathVariable String to,
                                                 @PathVariable BigDecimal quantity) {


        Map<String, String> uriVariables = new HashMap<>();

        uriVariables.put("from", from);
        uriVariables.put("to", to);

        final ResponseEntity<CurrencyConverter> responseEntity =
                new RestTemplate().getForEntity(INVOKE_URL, CurrencyConverter.class, uriVariables);
        CurrencyConverter response = responseEntity.getBody();

        logger.info("Currency Conversion {} ", response);

        return new CurrencyConverter(response.getId(), from, to,
                response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()), 0);
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConverter getCurrentConverterFeign(@PathVariable String from,
                                                      @PathVariable String to,
                                                      @PathVariable BigDecimal quantity) {


        CurrencyConverter response = proxy.getExchange(from, to);

        logger.info("Currency Conversion FEIGN {} ", response);

        return new CurrencyConverter(response.getId(), from, to,
                response.getConversionMultiple(),
                quantity,
                quantity.multiply(response.getConversionMultiple()),
                response.getPort());
    }
}
