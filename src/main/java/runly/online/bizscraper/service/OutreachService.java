package runly.online.bizscraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import runly.online.bizscraper.model.Business;
import runly.online.bizscraper.repository.BusinessRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OutreachService {

    final
    BusinessRepository businessRepository;


    public OutreachService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Transactional
    public Map<Long, String> any() {
        Business business = businessRepository.findById(businessRepository.findAnyPending()).get();
        log.info("Found {} business", business.getName());
        while (business.getWebsiteUrl() == null) {
            log.info("Business {} has no email, deleting {}...", business.getName(), business.getName());
            businessRepository.delete(business);
            business = businessRepository.findById(businessRepository.findAnyPending()).get();
            log.info("Found {} business", business.getName());
        }
        Map<Long, String> map = new HashMap<>();
        map.put(business.getId(), business.getWebsiteUrl());
        business.setStatus("forming_email");
        return map;
    }

    public Business verifyBusiness(Long id) {
        log.info("Verifying business {}...", id);
        return businessRepository.findById(id).get();
    }

    @Transactional
    public void changeStatus(Business business, String emailBody) {
        log.info("Changing status of business {}", business.getName());
        business.setStatus("email-sent");
        business.setEmailSent(true);
        business.setEmailBody(emailBody);
        business.setSentAt(LocalDateTime.now());
    }

    public void formEmail() {
        log.info("Forming email...");
    }
}
