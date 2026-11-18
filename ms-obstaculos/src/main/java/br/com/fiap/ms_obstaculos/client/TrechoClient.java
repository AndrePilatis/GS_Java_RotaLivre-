package br.com.fiap.ms_obstaculos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ms-trechos")
public interface TrechoClient {

    @RequestMapping(method = RequestMethod.PUT,
            value = ("/api/v1/trechos/{trechoId}/bloquear"))
    void bloquearTrecho(@PathVariable Long trechoId);

    @RequestMapping(method = RequestMethod.PUT,
            value = ("/api/v1/trechos/{trechoId}/restringir"))
    void restringirTrecho(@PathVariable Long trechoId);

    @RequestMapping(method = RequestMethod.PUT,
            value = ("/api/v1/trechos/{trechoId}/liberar"))
    void liberarTrecho(@PathVariable Long trechoId);
}
