package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class FakeCheckoutRequestTest {

	@Test
	void testGettersAndSetters() {
		FakeCheckoutRequest.ItemProduto item = new FakeCheckoutRequest.ItemProduto();
		item.setIdProduto(1L);
		item.setQuantidade(2);
		FakeCheckoutRequest req = new FakeCheckoutRequest();
		req.setIdCliente(10L);
		req.setProdutos(List.of(item));
		assertEquals(10L, req.getIdCliente());
		assertEquals(1L, req.getProdutos().get(0).getIdProduto());
		assertEquals(2, req.getProdutos().get(0).getQuantidade());
	}

	@Test
	void testNullAndEmptyProdutos() {
		FakeCheckoutRequest req = new FakeCheckoutRequest();
		assertNull(req.getProdutos());
		req.setProdutos(Collections.emptyList());
		assertTrue(req.getProdutos().isEmpty());
	}

	@Test
	void testEqualsAndHashCodeItemProduto() {
		FakeCheckoutRequest.ItemProduto item1 = new FakeCheckoutRequest.ItemProduto();
		item1.setIdProduto(1L);
		item1.setQuantidade(2);
		FakeCheckoutRequest.ItemProduto item2 = new FakeCheckoutRequest.ItemProduto();
		item2.setIdProduto(1L);
		item2.setQuantidade(2);
		assertEquals(item1, item2);
		assertEquals(item1.hashCode(), item2.hashCode());
	}

	@Test
	void testToString() {
		FakeCheckoutRequest.ItemProduto item = new FakeCheckoutRequest.ItemProduto();
		item.setIdProduto(1L);
		item.setQuantidade(2);
		FakeCheckoutRequest req = new FakeCheckoutRequest();
		req.setIdCliente(10L);
		req.setProdutos(List.of(item));
		String str = req.toString();
		assertFalse(str.contains("idCliente=10") || str.contains("10"));
	}
}
