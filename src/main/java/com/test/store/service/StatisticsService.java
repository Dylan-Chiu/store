package com.test.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatisticsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 返回的数据是0时，1时，2时....的成交额
     *
     * @return
     */
    public double[] getHourTurnover() {
        //获取当天日期
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = simpleDateFormat.format(date);

        double turnoverArray[] = new double[24];
        String sql = "SELECT HOUR(order_time) `hour`,sum(price*amount) `turnover`  \n" +
                "FROM `order`,order_detail,goods                            \n" +
                "WHERE `order`.order_id = order_detail.order_id             \n" +
                "AND order_detail.goods_id = goods.id                       \n" +
                "AND date(order_time) = ?                                   \n" +
                "GROUP BY HOUR(order_time)                                    ";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, dateStr);
        for (int i = 0; i < data.size(); i++) {
            Integer hour = (Integer) data.get(i).get("hour");
            Double turnover = Double.valueOf(data.get(i).get("turnover").toString());
            turnoverArray[hour] = turnover;
        }
        return turnoverArray;
    }

    /**
     * 返回的数据是 6天前、5天前、4天前、3天前、2天前、1天前、当天的成交额
     *
     * @return
     */
    public double[] getWeekTurnover() {
        String sql = "SELECT date(order_time) `date` ,sum(price*amount) `turnover`\n" +
                "FROM `order`,order_detail,goods\n" +
                "WHERE `order`.order_id = order_detail.order_id\n" +
                "AND order_detail.goods_id = goods.id\n" +
                "GROUP BY date(order_time)";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);

        //获取7天日期date
        Date dateArray[] = new Date[7];
        dateArray[6] = new Date();
        for (int i = 5; i >= 0; i--) {
            dateArray[i] = new Date(dateArray[i + 1].getTime() - 1 * 24 * 3600 * 1000L);
        }

        //把数据放进一个Map里
        HashMap<String, Double> turnoverMap = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            String date = data.get(i).get("date").toString();
            Double turnover = Double.valueOf(data.get(i).get("turnover").toString());
            turnoverMap.put(date, turnover);
        }
        System.out.println(turnoverMap);

        //自动转为'yyyy-MM-dd'去数据库查询结果(turnoverMap)里面找,然后放进turnoverArray里
        double turnoverArray[] = new double[7];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < dateArray.length; i++) {
            String strDate = simpleDateFormat.format(dateArray[i]);
            Double turnover = turnoverMap.get(strDate);
            if (turnover != null) {
                turnoverArray[i] = turnover;
            }
        }
        return turnoverArray;
    }

    /**
     * 返回值是 Map<String, List> result
     * result.get("goodsList") 这个能拿到前几个的商品名称
     * result.get("amountList") 这个能拿到前几个的销售总量
     * goodsList和amountList是一一对应的
     *
     * @param top 获取排行榜前top个
     * @param day 1是今天，2是昨天加今天，3是前天加昨天加今天 以此类推
     * @return
     */
    public Map<String, List> getTopGoods(int top, int day) {
        //获取截至日期 截至日期是今天的下一天 比如我今天18点买了东西，右边界要设成明天0点才能把今天的这个包括进去
        Date rightDate = new Date();
        rightDate = new Date(rightDate.getTime() + 1 * 24 * 3600 * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //获取左边界时间
        Date leftDate = new Date(rightDate.getTime() - day * 24 * 3600 * 1000L);

        //获取左右时间字符串
        String rightDateStr = simpleDateFormat.format(rightDate) + " 00:00:00";
        String leftDateStr = simpleDateFormat.format(leftDate) + " 00:00:00";

        String sql_part_for_time = "AND order_time BETWEEN \'" + leftDateStr + "\' AND \'" + rightDateStr + "\'\n";

        String sql = "SELECT `name`,sum(amount) amount\n" +
                "FROM `order`,order_detail,goods\n" +
                "WHERE `order`.order_id = order_detail.order_id\n" +


//                "AND DATE(order_time) BETWEEN ? AND ?" + //这个jdbcTemplate 有bug 把这个直接这样写会查不到数据，这里强行拼串
                sql_part_for_time +

                "AND order_detail.goods_id = goods.id\n" +
                "GROUP BY goods_id,`name`\n" +
                "ORDER BY sum(amount) DESC\n" +
                "LIMIT ?";
        System.out.println(leftDateStr);
        System.out.println(rightDateStr);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, top);
        List<String> goodsList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            goodsList.add(map.get("name").toString());
            amountList.add(Integer.valueOf(map.get("amount").toString()));
        }
        HashMap<String, List> result = new HashMap<>();
        result.put("goodsList", goodsList);
        result.put("amountList", amountList);
        return result;
    }

}
