package runly.online.bizscraper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import runly.online.bizscraper.dto.EmailSentRequest;
import runly.online.bizscraper.model.Business;
import runly.online.bizscraper.service.OutreachService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("runly/api/v1.0/bizscraper/outreach")
public class OutreachController {

    final
    OutreachService outreachService;

    public OutreachController(OutreachService outreachService) {
        this.outreachService = outreachService;
    }

    @PatchMapping("/business/any")
    public Map<Long, String> any() {
        Map<Long, String> business = outreachService.any();
        return business;
    }

    @PatchMapping("/business/email-sent")
    public void emailSent(@RequestBody EmailSentRequest request) {
        log.info("Email sent request to business with id: {}", request.toString());
        Business business = outreachService.verifyBusiness(request.getId());
        outreachService.changeStatus(business, request.getEmailBody());
    }
}
