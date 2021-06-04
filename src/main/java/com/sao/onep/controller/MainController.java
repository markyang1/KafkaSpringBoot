package com.sao.onep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sao.onep.kafka.KafKaProducerService;
import com.sao.onep.kafka.KafkaConsumerService;
import com.sao.onep.model.Candidate;

@Controller
@RequestMapping({ "/", "/index" })
public class MainController {
	
	private final KafKaProducerService producerService;
	private final KafkaConsumerService consumerService;
	private Candidate cddt;
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
    public MainController(KafKaProducerService producerService, KafkaConsumerService consumerService) 
    {
        this.producerService = producerService;
        this.consumerService = consumerService;
    }

    @GetMapping
    public String main(Model model) {
        Candidate candidate = new Candidate();
        model.addAttribute("candidate", candidate);

        return "index"; //view
    }
	

    @PostMapping
    public String response(Candidate cddt) {
        //model.addAttribute("birthplace", birthplace);
    	
    	this.cddt = cddt;
    	String cddtJson="";
    	try {
			cddtJson = mapper.writeValueAsString(this.cddt);
	    	this.producerService.sendMessage(cddtJson);
	        
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
    	return "response";
    }

    @GetMapping("/dashboard")
    public String home(Model model) {
		return "dashboard";
	}
    
}
