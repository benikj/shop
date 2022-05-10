package vb.shop.service;


import com.stripe.exception.*;
import com.stripe.model.Charge;
import vb.shop.model.dto.ChargeRequest;

public interface PaymentService {
    Charge pay(ChargeRequest chargeRequest) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException;
}
