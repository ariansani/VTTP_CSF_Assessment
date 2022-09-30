package vttp2022.assessment.csf.orderbackend.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderRestController {


    @Autowired
    private OrderService orderSvc;

    @GetMapping(path="/order/{email}/all")
    public ResponseEntity<String> getOrdersByEmail(@PathVariable String email){
        List<OrderSummary> orderSummaryList = new LinkedList<>();

        try {
       orderSummaryList = orderSvc.getOrdersByEmail(email);

        } catch (Exception e) {
            // TODO: handle exception
            JsonObject errJson = Json.createObjectBuilder()
            .add("errorMessage", e.getMessage()).build();
   return ResponseEntity.status(400).body(errJson.toString());
        }
   
        return ResponseEntity.ok(OrderSummary.toJson(orderSummaryList).toString());


    }

    @PostMapping(path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addOrder(@RequestBody String payload) {
        JsonObject responseJson;
        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {

            JsonReader reader = Json.createReader(is);
            JsonObject o = reader.readObject();
    
       
            Order order = new Order();
            order.setName(o.getString("name"));
            order.setEmail(o.getString("email"));
            order.setSize(o.getInt("size"));
            order.setSauce(o.getString("sauce"));
            order.setThickCrust(o.getBoolean("base"));

            List<String> arrayToppings = new LinkedList<>();
            
            JsonArray ja =  o.getJsonArray("toppings");

            arrayToppings.add(ja.toString());
            System.out.println("this is arrayToppings "+arrayToppings);
            order.setToppings(arrayToppings);

            order.setComments(o.getString("comments"));
           
            System.out.println(">>>>>order: "+order.getToppings());

            boolean addedSuccessfully = orderSvc.addOrder(order);

            responseJson = Json.createObjectBuilder()
            .add("status", addedSuccessfully)
            .build();

        } catch (Exception ex) {
            JsonObject errJson = Json.createObjectBuilder()
                    .add("errorMessage", ex.getMessage()).build();
           return ResponseEntity.status(400).body(errJson.toString());
        }
        return ResponseEntity.ok(responseJson.toString());

    }


}
