package com.anderfred.assignmenttask.elasticrepo;

import com.anderfred.assignmenttask.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface OrderSearchRepository extends ElasticsearchRepository<Order, Long> {

    @Query("{\"match_phrase\": {\"orderItems.product.name\": \"*?0*\"}}")
    Page<Order> findByProductNameCustom(String name, Pageable pageable);
}
