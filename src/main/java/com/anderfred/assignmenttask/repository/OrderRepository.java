package com.anderfred.assignmenttask.repository;

import com.anderfred.assignmenttask.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select date_trunc('day' , created_at)   as created_date, SUM(total_amount) as order_total from public.order group by 1 ORDER BY 1", nativeQuery = true)
    List<Object[]> findReportData();
}
