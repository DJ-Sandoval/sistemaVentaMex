package com.VentaMex.apiVentaMex.util.components;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MetadataInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetails(Map.of(
                "name", "apiVentaMex",
                "description","api para la gestion de un sistema de ventas",
                "autor", "DevSandoval DevSofi",
                "proyecto", "VentaMex API",
                "tecnologia", "Spring Boot + MySQL + docker + jenkins",
                "version","2.5"
        ));
    }
}

