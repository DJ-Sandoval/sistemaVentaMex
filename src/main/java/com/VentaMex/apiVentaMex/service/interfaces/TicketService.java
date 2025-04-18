package com.VentaMex.apiVentaMex.service.interfaces;

public interface TicketService {
    String generarTicketPdf(Long ventaId) throws Exception;
    String generarTicketTexto(Long ventaId);
}
