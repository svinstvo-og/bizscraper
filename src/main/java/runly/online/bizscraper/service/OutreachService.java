package runly.online.bizscraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import runly.online.bizscraper.dto.EmailGeneratedResponse;
import runly.online.bizscraper.model.Business;
import runly.online.bizscraper.repository.BusinessRepository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class OutreachService {

    final BusinessRepository businessRepository;
    final EmailService emailService;
    final RequestService requestService;

    static Set<String> seenEmails = new HashSet<>();

    public OutreachService(BusinessRepository businessRepository, RequestService requestService, EmailService emailService) {
        this.businessRepository = businessRepository;
        this.requestService = requestService;
        this.emailService = emailService;
        seenEmails = businessRepository.findDistinctEmails();
    }

    @Transactional
    public Map<Long, String> getAny() {
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
        //business.setStatus("forming_email");
        return map;
    }

    @Transactional
    public List<Business> getPendingBusinesses(int count) {
        List<Business> businesses = businessRepository.findTopNByStatus("pending", PageRequest.of(0, count));
//        for (Business business : businesses) {
//            log.info("Found {} business", business.getName());
//        }
        return businesses;
    }

    public Business verifyBusiness(Long id) {
        log.info("Verifying business {}...", id);
        return businessRepository.findById(id).get();
    }

    @Transactional
    public void emailSent(Business business, String email) {
        log.info("Changing status of business {}, email sent successfully", business.getName());
        business.setStatus("EMAIL-SENT");
        business.setEmail(email);
        business.setEmailSent(true);
        business.setSentAt(LocalDateTime.now());
    }

    public void formEmail() {
        log.info("Forming email...");
        //TODO
    }

    public Business validateBusiness(Long id) {
        log.info("Validating business {}", id);
        Optional<Business> business = businessRepository.findById(id);
        if (business.isPresent()) {
            log.info("Found business {}", business.get().getName());
            return business.get();
        }
        throw new RuntimeException("Business not found");
    }

    @Transactional
    public void updateStatus(Business business, String status) {
        log.info("Updating status of business {}", business.getName());
        business.setStatus(status);
    }

    public void outreach(List<Business> businesses) {
        log.info("Seen {} emails", seenEmails.size());
        int i = 0;
        for (Business business : businesses) {
            try {
                log.info("#{}, Outreaching business {}...", ++i, business.getName());
                processSingleBusiness(business);
            } catch (Exception e) {
                log.error("Failed to process business {}: {}", business.getName(), e.getMessage(), e);
                // Optionally: mark business as 'PROCESSING-FAILED'
                business.setStatus("PROCESSING-FAILED");
                business.setUpdatedAt(LocalDateTime.now());
            }
        }
    }

    @Transactional
    public void processSingleBusiness(Business business) {
        ResponseEntity<EmailGeneratedResponse> responseEntity =
                requestService.generateEmail(business.getId(), business.getWebsiteUrl(), business.getCountry());

        if (responseEntity.getStatusCode().value() == 200) {
            EmailGeneratedResponse response = responseEntity.getBody();
            if (response == null || seenEmails.contains(business.getName()) || !response.getEmail().contains("@")) {
                business.setStatus("EMAIL-SENDING-ERROR");
                business.setUpdatedAt(LocalDateTime.now());
                businessRepository.save(business);
                return;
            }

            int statusCode = emailService.sendEmail(response.getEmail(), response.getSubject(), response.getBody());
            if (statusCode == 200) {
                business.setStatus("EMAIL-SENT");
                business.setUpdatedAt(LocalDateTime.now());
                business.setEmailSent(true);
                business.setEmail(response.getEmail());
                businessRepository.save(business);
                seenEmails.add(response.getEmail());
            } else {
                business.setStatus("PROCESSING-FAILED");
                business.setUpdatedAt(LocalDateTime.now());
                businessRepository.save(business);
            }

        } else if (responseEntity.getStatusCode().value() == 204) {
            business.setStatus("NO-EMAIL-FOUND");
            business.setUpdatedAt(LocalDateTime.now());
            businessRepository.save(business);
        } else {
            business.setStatus("EMAIL-SENDING-ERROR");
            business.setUpdatedAt(LocalDateTime.now());
            businessRepository.save(business);
        }
    }
}
