package vttp2022.assessment.csf.orderbackend.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;

@Repository
public class OrderRepository {

    private static final String SQL_GET_ORDERS_BY_EMAIL = "SELECT * FROM orders WHERE email = ?";
    private static final String SQL_ADD_ORDER = "INSERT INTO orders (name,email, pizza_size,sauce ,thick_crust,toppings, comments) VALUES(?,?,?,?,?,?,?)";
    
    
    @Autowired
    private JdbcTemplate template;

    public List<Order> getOrders(String email) {
        List<Order> orderList = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDERS_BY_EMAIL, email);
        while (rs.next()) {
            Order order = Order.create(rs);
            orderList.add(order);

        }
        return orderList;

    }

    public boolean addOrder(Order order,String orderRearrange){

        int count = template.update(SQL_ADD_ORDER, order.getName(),order.getEmail(),order.getSize(), order.getSauce(),order.isThickCrust(), orderRearrange, order.getComments());

        return 1 == count;
    }

    
}
