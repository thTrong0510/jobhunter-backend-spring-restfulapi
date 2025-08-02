package com.jobhunter.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobhunter.jobhunter.service.EmailService;
import com.jobhunter.jobhunter.service.SubscriberService;
import com.jobhunter.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("2251052129trong@ou.edu.vn", "Test send
        // mail", "<h1><b>Hello<B/></h1>", false,
        // true);
        // this.emailService.sendEmailFromTemplateSync("2251052129trong@ou.edu.vn",
        // "Test send mail", "job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "Already send";
    }
}
