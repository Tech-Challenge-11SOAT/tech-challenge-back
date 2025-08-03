package br.com.postech.techchallange.application.service;

import br.com.postech.techchallange.adapter.in.rest.request.WebhookPagamentoRequest;
import br.com.postech.techchallange.domain.enums.StatusPagamentoEnum;
import br.com.postech.techchallange.domain.model.Pagamento;
import br.com.postech.techchallange.domain.model.Pedido;
import br.com.postech.techchallange.domain.port.in.GerenciarPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarPedidoUseCase;
import br.com.postech.techchallange.domain.port.in.GerenciarStatusPagamentoUseCase;
import br.com.postech.techchallange.domain.port.in.PagamentoValidatorPort;
import br.com.postech.techchallange.domain.port.out.PagamentoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GerenciarPagamentoService implements GerenciarPagamentoUseCase {

    private final PagamentoRepositoryPort repository;
    private final GerenciarStatusPagamentoUseCase gerenciarStatusPagamento;
    private final GerenciarPedidoUseCase gerenciarPedidoUseCase;
    private final PagamentoValidatorPort pagamentoValidator;
    private final NotificacaoPagamentoService notificacaoService;

    @Override
    public Pagamento pagar(Pagamento pagamento) {
        Pedido pedido = this.gerenciarPedidoUseCase.buscarPedido(pagamento.getIdPedido());
        this.pagamentoValidator.validar(pedido, pagamento);

        pagamento.setDataPagamento(LocalDateTime.now());
        long idStatusPendente = this.gerenciarStatusPagamento
                .buscarStatusPagamentoPorStatus(StatusPagamentoEnum.PENDENTE.getStatus())
                .getIdStatusPagamento();

        pagamento.setIdStatusPagamento(idStatusPendente);

        Pagamento salvo = this.repository.salvar(pagamento);

        // Pagamento preferenciaCriada = mercadoPagoPort.criarPreferenciaPagamento(salvo);
        repository.salvar(salvo);

        return salvo;
    }

    @Override
    public Pagamento buscarPagamento(Long id) {
        return repository.buscarPorId(id).orElse(null);
    }

    @Override
    public List<Pagamento> listarPagamentos() {
        return repository.listarTodos();
    }

    @Override
    public Pagamento buscarPagamentoPorPedido(Long idPedido) {
        return repository.buscarPorIdPedido(idPedido).orElse(null);
    }

    @Override
    public void processarWebhookPagamento(WebhookPagamentoRequest request) {
        Long idPedido = this.getIdPedido(request.getData().getExternalReference());
        Pagamento pagamento = repository.buscarPorIdPedido(idPedido)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado para o pedido informado"));

        String statusPagamento = this.verifyOrderStatus(request.getData())
                ? StatusPagamentoEnum.FINALIZADO.getStatus()
                : StatusPagamentoEnum.ERRO.getStatus();

        Long novoStatusId = this.gerenciarStatusPagamento.buscarStatusPagamentoPorStatus(statusPagamento).getIdStatusPagamento();

        pagamento.setIdStatusPagamento(novoStatusId);
        pagamento.setDataPagamento(LocalDateTime.now());
        repository.salvar(pagamento);

        // Notifica todos os listeners (incluindo o WebSocketHandler) via observer pattern
        notificacaoService.notificarMudancaStatus(idPedido);
    }

    private Long getIdPedido(String ext) {
        if (ext == null || ext.isBlank() || !ext.contains("_")) {
            throw new IllegalArgumentException("String inválida");
        }

        String[] parts = ext.split("_");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Formato da string inválido: esperado pelo menos 3 partes separadas por '_'");
        }

        try {
            return Long.parseLong(parts[2]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID do pedido inválido: não é um número válido", ex);
        }
    }

    private boolean verifyOrderStatus(WebhookPagamentoRequest.OrderData data) {
        return "processed".equalsIgnoreCase(data.getStatus())
                && "accredited".equalsIgnoreCase(data.getStatusDetail());
    }

}
