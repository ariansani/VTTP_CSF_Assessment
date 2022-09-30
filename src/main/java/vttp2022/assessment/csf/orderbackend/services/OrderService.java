package vttp2022.assessment.csf.orderbackend.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private PricingService priceSvc;

	@Autowired
	private OrderRepository orderRepo;

	// POST /api/order
	// Create a new order by inserting into orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public boolean addOrder(Order order) {
		String orderRearrange = order.getToppings().toString();
		String orderRearrangeIt = orderRearrange.substring(2,orderRearrange.length()-2);
	
		return orderRepo.addOrder(order,orderRearrangeIt);
	}

	// GET /api/order/<email>/all
	// Get a list of orders for email from orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public List<OrderSummary> getOrdersByEmail(String email) {
		// Use priceSvc to calculate the total cost of an order
		List<OrderSummary> orderSummaryList = new LinkedList<>();
		
		List<Order> orderList = orderRepo.getOrders(email);
		
		for (Order order : orderList) {
			Float amount = 0.0f;
			amount+=priceSvc.sauce(order.getSauce());
			amount+=priceSvc.size(order.getSize());
			amount+= order.isThickCrust() ? priceSvc.thickCrust(): priceSvc.thinCrust();
			List<String> toppingsList = order.getToppings();
			for (String toppings : toppingsList) {
				amount+=priceSvc.topping(toppings);
				System.out.println(toppings);
			}
			OrderSummary os = new OrderSummary();
			os.setAmount(amount);
			os.setEmail(order.getEmail());
			os.setName(order.getName());
			os.setOrderId(order.getOrderId());
			orderSummaryList.add(os);
		}
		return orderSummaryList;
		
		
	}
}
