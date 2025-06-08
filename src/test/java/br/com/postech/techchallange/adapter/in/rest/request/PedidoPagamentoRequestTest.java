package br.com.postech.techchallange.adapter.in.rest.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class PedidoPagamentoRequestTest {

	@Test
	void testAllArgsConstructorAndGetters() {
		PedidoPagamentoRequest.ItemProdutoRequest item = new PedidoPagamentoRequest.ItemProdutoRequest(1L, 2);
		List<PedidoPagamentoRequest.ItemProdutoRequest> produtos = List.of(item);
		PedidoPagamentoRequest req = new PedidoPagamentoRequest(10L, "PIX", produtos, 5);
		assertEquals(10L, req.getIdCliente());
		assertEquals("PIX", req.getMetodoPagamento());
		assertEquals(produtos, req.getProdutos());
		assertEquals(5, req.getFilaPedido());
	}

	@Test
	void testNoArgsConstructorAndSetters() {
		PedidoPagamentoRequest req = new PedidoPagamentoRequest();
		req.setIdCliente(20L);
		req.setMetodoPagamento("CARTAO");
		PedidoPagamentoRequest.ItemProdutoRequest item = new PedidoPagamentoRequest.ItemProdutoRequest();
		item.setIdProduto(2L);
		item.setQuantidade(3);
		req.setProdutos(List.of(item));
		req.setFilaPedido(7);
		assertEquals(20L, req.getIdCliente());
		assertEquals("CARTAO", req.getMetodoPagamento());
		assertEquals(2L, req.getProdutos().get(0).getIdProduto());
		assertEquals(3, req.getProdutos().get(0).getQuantidade());
		assertEquals(7, req.getFilaPedido());
	}

	@Test
	void testEqualsAndHashCode() {
		PedidoPagamentoRequest.ItemProdutoRequest item1 = new PedidoPagamentoRequest.ItemProdutoRequest(1L, 2);
		PedidoPagamentoRequest.ItemProdutoRequest item2 = new PedidoPagamentoRequest.ItemProdutoRequest(1L, 2);
		assertEquals(item1, item2);
		assertEquals(item1.hashCode(), item2.hashCode());
		PedidoPagamentoRequest req1 = new PedidoPagamentoRequest(1L, "PIX", List.of(item1), 1);
		PedidoPagamentoRequest req2 = new PedidoPagamentoRequest(1L, "PIX", List.of(item2), 1);
		assertEquals(req1, req2);
		assertEquals(req1.hashCode(), req2.hashCode());
	}

	@Test
	void testToString() {
		PedidoPagamentoRequest.ItemProdutoRequest item = new PedidoPagamentoRequest.ItemProdutoRequest(1L, 2);
		PedidoPagamentoRequest req = new PedidoPagamentoRequest(1L, "PIX", List.of(item), 1);
		String str = req.toString();
		assertTrue(str.contains("idCliente=1"));
		assertTrue(str.contains("metodoPagamento=PIX"));
		assertTrue(str.contains("produtos"));
	}

	@Test
	void testNullAndEmptyProdutos() {
		PedidoPagamentoRequest req = new PedidoPagamentoRequest(1L, "PIX", null, null);
		assertNull(req.getProdutos());
		req.setProdutos(Collections.emptyList());
		assertTrue(req.getProdutos().isEmpty());
	}

	@Test
	void testItemProdutoRequestNoArgsAndSetters() {
		PedidoPagamentoRequest.ItemProdutoRequest item = new PedidoPagamentoRequest.ItemProdutoRequest();
		item.setIdProduto(10L);
		item.setQuantidade(5);
		assertEquals(10L, item.getIdProduto());
		assertEquals(5, item.getQuantidade());
	}
}
