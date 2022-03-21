package org.devgateway.toolkit.web.rest.controller;

import io.swagger.annotations.ApiOperation;
import org.devgateway.toolkit.persistence.dto.TetsimOutputs;
import org.devgateway.toolkit.persistence.service.tetsim.TetsimOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vchihai
 */

@RestController
public class TetsimController {

    private static final Logger logger = LoggerFactory.getLogger(TetsimController.class);

    private final TetsimOutputService tetsimOutputService;

    public TetsimController(final TetsimOutputService tetsimOutputService) {
        this.tetsimOutputService = tetsimOutputService;
    }

    @ApiOperation(value = "TETSIM Output API")
    @RequestMapping(value = "{dg-toolkit.forms.base-path}/api/tetsim/output", method = RequestMethod.GET,
            produces = "application/json")
    public TetsimOutputs getTetsimOutput(@RequestParam() final Long id) {
        TetsimOutputs outputs = tetsimOutputService.getTetsimOutputs(id);

        return outputs;
    }

}
