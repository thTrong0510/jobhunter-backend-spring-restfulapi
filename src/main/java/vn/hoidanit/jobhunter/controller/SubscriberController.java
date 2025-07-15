package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.exception.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriberRequest)
            throws IdInvalidException {
        if (this.subscriberService.isExistEmail(subscriberRequest.getEmail())) {
            throw new IdInvalidException("Subscriber with email: " + subscriberRequest.getEmail() + " is exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.subscriberService.createSubscriber(subscriberRequest));
    }

    @PutMapping("/subscribers")
    @ApiMessage("update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriberRequest)
            throws IdInvalidException {
        Subscriber subscriberDB = this.subscriberService.fetchById(subscriberRequest.getId());
        if (subscriberDB == null) {
            throw new IdInvalidException("Subscriber with id: " + subscriberRequest.getId() + " is not exist");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.subscriberService.updateSubscriber(subscriberDB, subscriberRequest));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscriberSkill() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        return ResponseEntity.ok().body(this.subscriberService.fetchByEmail(email));
    }
}
