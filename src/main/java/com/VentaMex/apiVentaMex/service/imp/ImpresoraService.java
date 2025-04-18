package com.VentaMex.apiVentaMex.service.imp;

import org.springframework.stereotype.Service;
import javax.print.*;
import javax.print.attribute.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class ImpresoraService {

    public void imprimirTicket(String contenido) throws PrintException, IOException {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        PrintService impresoraDestino = null;
        for (PrintService service : services) {
            if (service.getName().toLowerCase().contains("Epson")) {
                impresoraDestino = service;
                break;
            }
        }

        if (impresoraDestino == null) {
            impresoraDestino = PrintServiceLookup.lookupDefaultPrintService();
        }

        if (impresoraDestino == null) {
            throw new PrintException("No se encontró una impresora disponible.");
        }

        DocPrintJob job = impresoraDestino.createPrintJob();
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Centrar
        outputStream.write(new byte[]{0x1B, 0x61, 1});
        // Negrita ON
        outputStream.write(new byte[]{0x1B, 0x45, 1});
        outputStream.write("VentaMex POS v.25\n".getBytes(StandardCharsets.UTF_8));
        // Negrita OFF
        outputStream.write(new byte[]{0x1B, 0x45, 0});

        // Izquierda
        outputStream.write(new byte[]{0x1B, 0x61, 0});
        outputStream.write(contenido.getBytes(StandardCharsets.UTF_8));

        // 3 saltos de línea
        outputStream.write("\n\n\n".getBytes(StandardCharsets.UTF_8));

        // Corte automático
        outputStream.write(new byte[]{0x1D, 'V', 1});

        // Convertir a bytes
        byte[] bytes = outputStream.toByteArray();

        Doc doc = new SimpleDoc(bytes, flavor, null);
        job.print(doc, null);
    }


}
