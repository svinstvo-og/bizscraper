package runly.online.bizscraper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import runly.online.bizscraper.dto.EmailSentRequest;
import runly.online.bizscraper.model.Business;
import runly.online.bizscraper.repository.BusinessRepository;
import runly.online.bizscraper.service.OutreachService;
import runly.online.bizscraper.service.RequestService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("runly/api/v1.0/bizscraper/outreach")
public class OutreachController {

    final
    OutreachService outreachService;

    final
    RequestService requestService;
    private final BusinessRepository businessRepository;

    public OutreachController(OutreachService outreachService, RequestService requestService, BusinessRepository businessRepository) {
        this.outreachService = outreachService;
        this.requestService = requestService;
        this.businessRepository = businessRepository;
    }

    @PatchMapping("/business/any")
    public Map<Long, String> any() {
        Map<Long, String> business = outreachService.getAny();
        return business;
    }

    @PatchMapping("/business/email/sent")
    public void emailSent(@RequestBody EmailSentRequest request) {
        log.info("Email sent request to business with id: {}", request.toString());
        Business business = outreachService.verifyBusiness(request.getId());
        outreachService.changeStatus(business, request.getEmailBody());
    }

    @PostMapping
    public void outreach() {
        outreachService.formEmail();
        //TODO
    }

    @PatchMapping("/{businessId}/{status}")
    public void updateStatus(@PathVariable Long businessId, @PathVariable String status) {
        log.info("Updating status of business with id: {}", businessId);
        Business business = outreachService.validateBusiness(businessId);
        outreachService.updateStatus(business, status);
        log.info("Updated status of business with id: {}", businessId);
    }

    @PatchMapping("/email/single")
    public void sendSingleEmail() {
        log.info("Sending single email");
        Map<Long, String> anyId = outreachService.getAny();
        Long id = anyId.keySet().iterator().next();
        Business business = outreachService.validateBusiness(id);
        requestService.generateEmail(id, business.getWebsiteUrl(), business.getCountry());
    }
}
