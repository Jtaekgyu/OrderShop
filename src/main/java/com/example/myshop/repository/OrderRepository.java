package com.example.myshop.repository;

import com.example.myshop.domain.Member;
import com.example.myshop.domain.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 아래처럼 fetch join을 걸 수 도 있지만 지금은 MemberId에 의해서만 Order를 가져오는 거라서 굳이 fetch join을 걸지 않았다.
//    @Query("select distinct o from Order o"+
//            " join fetch o.member m"+
//            " join fetch o.delivery d"+
//            " join fetch o.orderItems oi"+
//            " join fetch oi.item")
    List<Order> findAllByMemberId(Long id);
}
