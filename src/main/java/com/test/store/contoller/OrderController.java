package com.test.store.contoller;

import com.alibaba.fastjson.JSON;
import com.test.store.entity.Order;
import com.test.store.entity.OrderDetail;
import com.test.store.service.OrderService;
import com.test.store.util.StatusCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/Order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("createOrder")
    public String createOrder(HttpServletRequest request, @RequestBody Map<String, Object> params) {

        //获取订单内商品信息
        List<Map> goodsList = (List<Map>) params.get("goodsList");
        List<OrderDetail> detail = new ArrayList<OrderDetail>();
        for (Map map : goodsList) {
            int id = (int) map.get("id");
            int amount = (int) map.get("num");
            if (amount <= 0) {
                continue;
            }
            OrderDetail d = new OrderDetail(id, amount);
            detail.add(d);
        }

        //获取用户名
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {//前端页面有判断了，这里简单判断一下
            return StatusCodeUtil.getCodeJsonString(StatusCodeUtil.USERNAME_ERROR);
        }

        //创建Order对象传给Service层
        Order order = new Order();
        order.setUsername(username);
        order.setDetail(detail);
        return orderService.createOrder(order);
    }

    @RequestMapping("getLimitOrders")
    public String getOrder(HttpServletRequest request,
                           @RequestParam(value = "page", required = false) String strPage,
                           @RequestParam(value = "limit", required = false) String strLimit) {

        HashMap<String, Object> message = new HashMap<String, Object>();

        //获取当前页码
        int curPage = 1;
        if (strPage != null) {
            curPage = Integer.valueOf(strPage);
        }

        //获取当页商品数
        int limit = 12;
        if (strLimit != null) {
            limit = Integer.valueOf(strLimit);
        }

        message.put("code", 0);
        message.put("msg", "");
        message.put("curPage", curPage);
        message.put("count", orderService.getCount());
        List<Order> orderList = orderService.getLimitOrder(curPage, limit);
        message.put("data", orderList);
        return JSON.toJSONString(message);

    }

    @RequestMapping("finishOrder")
    public String finishOrder(HttpServletRequest request,
                              @RequestParam(value = "page", required = false) String strPage,
                              @RequestParam(value = "limit", required = false) String strLimit,
                              @RequestBody Map<String, Object> params) {

        String order_id = (String) params.get("order_id");
        final int ORDER_FINISH = 2;//2是已完成订单
        orderService.changeStatus(order_id, ORDER_FINISH);
        return getOrder(request, strPage, strLimit);
    }

    @RequestMapping("getDetailById")
    public String getOrderById(@RequestParam(value = "orderId") String id) {
        List<OrderDetail> detail = orderService.getDetailById(id);
        HashMap<String, Object> message = new HashMap<>();
        message.put("code",0);
        message.put("msg","");
        message.put("data",detail);
        return JSON.toJSONString(message);
    }
}
