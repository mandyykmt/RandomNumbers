package server.server.controllers;

import java.io.StringReader;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import server.server.services.RandomService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class RandomNumberController {

    @Autowired
    private RandomService randSvc;

    // GET /api/random?count=10
    @GetMapping(path="/random")
    @ResponseBody
    public ResponseEntity<String> getRandom(@RequestParam(defaultValue = "1") Integer count) {

        List<Integer> nums = randSvc.getRandomNumbers(count);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder(nums);

        JsonObject resp = Json.createObjectBuilder()
                            .add("numbers", arrBuilder.build())
                            .add("timestamp", (new Date()).toString())
                            .build();

        return ResponseEntity.ok(resp.toString());
    }   

    // POST /api/random
    // Content-Type: application/json
    // { count: 10, min: -100, max: 100 }
    @PostMapping(path="/random", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> postRandom(@RequestBody String payload) {
        
        JsonReader jsonReader = Json.createReader(new StringReader(payload));

        JsonObject req = jsonReader.readObject();

        int count = req.getInt("count", 1);
        int min = req.getInt("max", 0);
        int max = req.getInt("max", 10); 

        List<Integer> nums = randSvc.getRandomNumbers(min, max, count);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder(nums);

        JsonObject resp = Json.createObjectBuilder()
                            .add("numbers", arrBuilder.build())
                            .add("timestamp", (new Date()).toString())
                            .build(); 

        return ResponseEntity.ok(resp.toString());
    }
    
    // POST /api/random
    // Content-Type: application/x-www-form-urlencoded
    // count=10&min=-10&max=100
    @PostMapping(path="/random", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<String> postRandomForm(@RequestBody MultiValueMap<String, String> form) {

        int count = getValue(form.getFirst("count"), 1);
        int min = getValue(form.getFirst("min"), 0);
        int max = getValue(form.getFirst("max"), 10); 

        List<Integer> nums = randSvc.getRandomNumbers(min, max, count);
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(nums);

        JsonObject resp = Json.createObjectBuilder()
                            .add("numbers", arrayBuilder.build())
                            .add("timestamp", (new Date()).toString())
                            .build(); 

        return ResponseEntity.ok(resp.toString());
    }

    private int getValue(String value, int defValue) {
        if(value.trim().length() <= 0 || (null == value)) {
            return defValue;
        }
        return Integer.parseInt(value);
    }

}
