package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class WebhookPagamentoRequestTest {

    @Test
    void testGettersAndSetters() {
        WebhookPagamentoRequest req = new WebhookPagamentoRequest();
        req.setAction("payment.updated");
        req.setApiVersion("v1");
        req.setApplicationId("123456");
        req.setType("payment");
        req.setUserId("user123");
        req.setLiveMode(false);

        Instant now = Instant.now();
        req.setDateCreated(now);

        // Testando OrderData
        WebhookPagamentoRequest.OrderData orderData = new WebhookPagamentoRequest.OrderData();
        orderData.setExternalReference("pedido_123_456789");
        orderData.setId("12345");
        orderData.setStatus("approved");
        orderData.setStatusDetail("accredited");
        orderData.setTotalAmount("100.00");
        orderData.setTotalPaidAmount("100.00");
        orderData.setType("order");
        orderData.setVersion(1);

        req.setData(orderData);

        assertEquals("payment.updated", req.getAction());
        assertEquals("v1", req.getApiVersion());
        assertEquals("123456", req.getApplicationId());
        assertEquals("payment", req.getType());
        assertEquals("user123", req.getUserId());
        assertEquals(false, req.getLiveMode());
        assertEquals(now, req.getDateCreated());
        assertNotNull(req.getData());
        assertEquals("pedido_123_456789", req.getData().getExternalReference());
        assertEquals("approved", req.getData().getStatus());
    }

    @Test
    void testOrderDataGettersAndSetters() {
        WebhookPagamentoRequest.OrderData orderData = new WebhookPagamentoRequest.OrderData();
        orderData.setExternalReference("pedido_456_789123");
        orderData.setId("order123");
        orderData.setStatus("pending");
        orderData.setStatusDetail("pending_payment");
        orderData.setTotalAmount("250.75");
        orderData.setTotalPaidAmount("0.00");
        orderData.setType("order");
        orderData.setVersion(2);

        assertEquals("pedido_456_789123", orderData.getExternalReference());
        assertEquals("order123", orderData.getId());
        assertEquals("pending", orderData.getStatus());
        assertEquals("pending_payment", orderData.getStatusDetail());
        assertEquals("250.75", orderData.getTotalAmount());
        assertEquals("0.00", orderData.getTotalPaidAmount());
        assertEquals("order", orderData.getType());
        assertEquals(2, orderData.getVersion());
    }

    @Test
    void testTransactionsAndPayment() {
        // Testando Payment
        WebhookPagamentoRequest.Payment payment = new WebhookPagamentoRequest.Payment();
        payment.setAmount("100.00");
        payment.setId("payment123");
        payment.setPaidAmount("100.00");
        payment.setStatus("approved");
        payment.setStatusDetail("accredited");

        // Testando PaymentMethod
        WebhookPagamentoRequest.PaymentMethod paymentMethod = new WebhookPagamentoRequest.PaymentMethod();
        paymentMethod.setId("pix");
        paymentMethod.setType("account_money");
        paymentMethod.setInstallments(1);
        payment.setPaymentMethod(paymentMethod);

        // Testando Reference
        WebhookPagamentoRequest.Reference reference = new WebhookPagamentoRequest.Reference();
        reference.setId("ref123");
        payment.setReference(reference);

        // Testando Transactions
        WebhookPagamentoRequest.Transactions transactions = new WebhookPagamentoRequest.Transactions();
        transactions.setPayments(Arrays.asList(payment));

        assertEquals("100.00", payment.getAmount());
        assertEquals("payment123", payment.getId());
        assertEquals("approved", payment.getStatus());
        assertNotNull(payment.getPaymentMethod());
        assertEquals("pix", payment.getPaymentMethod().getId());
        assertEquals(1, transactions.getPayments().size());
    }

    @Test
    void testEqualsAndHashCode() {
        WebhookPagamentoRequest req1 = new WebhookPagamentoRequest();
        req1.setAction("payment.updated");
        req1.setType("payment");

        WebhookPagamentoRequest.OrderData orderData1 = new WebhookPagamentoRequest.OrderData();
        orderData1.setExternalReference("pedido_123");
        orderData1.setStatus("approved");
        req1.setData(orderData1);

        WebhookPagamentoRequest req2 = new WebhookPagamentoRequest();
        req2.setAction("payment.updated");
        req2.setType("payment");

        WebhookPagamentoRequest.OrderData orderData2 = new WebhookPagamentoRequest.OrderData();
        orderData2.setExternalReference("pedido_123");
        orderData2.setStatus("approved");
        req2.setData(orderData2);

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());
    }

    @Test
    void testToString() {
        WebhookPagamentoRequest req = new WebhookPagamentoRequest();
        req.setAction("payment.updated");
        req.setType("payment");

        WebhookPagamentoRequest.OrderData orderData = new WebhookPagamentoRequest.OrderData();
        orderData.setExternalReference("pedido_123");
        req.setData(orderData);

        String str = req.toString();
        assertTrue(str.contains("action=payment.updated"));
        assertTrue(str.contains("type=payment"));
    }

    @Test
    void testNullFields() {
        WebhookPagamentoRequest req = new WebhookPagamentoRequest();
        assertNull(req.getAction());
        assertNull(req.getApiVersion());
        assertNull(req.getApplicationId());
        assertNull(req.getData());
        assertNull(req.getDateCreated());
        assertNull(req.getLiveMode());
        assertNull(req.getType());
        assertNull(req.getUserId());
    }

    @Test
    void testOrderDataNullFields() {
        WebhookPagamentoRequest.OrderData orderData = new WebhookPagamentoRequest.OrderData();
        assertNull(orderData.getExternalReference());
        assertNull(orderData.getId());
        assertNull(orderData.getStatus());
        assertNull(orderData.getStatusDetail());
        assertNull(orderData.getTotalAmount());
        assertNull(orderData.getTotalPaidAmount());
        assertNull(orderData.getTransactions());
        assertNull(orderData.getType());
        assertNull(orderData.getVersion());
    }
}
