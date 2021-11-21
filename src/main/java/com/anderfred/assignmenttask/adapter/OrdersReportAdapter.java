package com.anderfred.assignmenttask.adapter;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/***
 *      Adapter to convert an order report sql query result to string
 *      Response needs to be treated as ordinary JSONArray of strings
 */

public class OrdersReportAdapter {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final List<Object[]> data;

    public OrdersReportAdapter(List<Object[]> data) {
        this.data = data;
    }

    public String getReport() {
        JSONArray response = new JSONArray();
        if (data != null && !data.isEmpty()) {
            data.forEach(order -> response.put(String.format("%s:%s", sdf.format(order[0]), df.format(order[1]))));
        }
        return response.toString();
    }
}