package io.pivotal.bluegreendemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;

@SpringBootApplication
@Controller
public class BlueGreenDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlueGreenDemoApplication.class, args);
    }

    @Autowired
    private ObjectMapper json;

    @Value("${VCAP_APPLICATION:{}}")
    private String application;

    @Value("${VCAP_SERVICES:{}}")
    private String services;


    @RequestMapping("/")
    public String index(Model model) {
        try {
            LinkedHashMap<String, String> cfapp = json.readValue(application, LinkedHashMap.class);
            model.addAttribute("cfapp", cfapp);

            LinkedHashMap<String, String> cfservices = json.readValue(services, LinkedHashMap.class);
            String cfservicename = cfservices.keySet()
                                             .iterator()
                                             .next();
            String cfservice = cfservices.get(cfservicename);
            model.addAttribute("cfservices", cfservices);
            model.addAttribute("cfservicename", cfservicename);
            model.addAttribute("cfservice", cfservice);
        } catch(Exception ex) {
            // No services
            model.addAttribute("cfservice", "");
            model.addAttribute("cfservice", new LinkedHashMap());
        } return "index";
    }

}
