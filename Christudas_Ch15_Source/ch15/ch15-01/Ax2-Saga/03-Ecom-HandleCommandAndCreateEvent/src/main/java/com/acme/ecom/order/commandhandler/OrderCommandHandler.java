/*
 * Copyright (c) 2019/2020 Binildas A Christudas, Apress Media, LLC. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of the author, publisher or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. AUTHOR, PUBLISHER AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL THE AUTHOR,
 * PUBLISHER OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA,
 * OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF THE AUTHOR, PUBLISHER HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */
package com.acme.ecom.order.commandhandler;

import java.util.Random;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.acme.ecom.order.api.command.NewOrderCommand;
import com.acme.ecom.order.api.command.OrderStatusUpdateCommand;
import com.acme.ecom.order.model.Order;
import com.acme.ecom.order.model.OrderStatusEnum;
import com.acme.ecom.product.model.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:biniljava<[@>.]yahoo.co.in">Binildas C. A.</a>
 */
@Component
public class OrderCommandHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCommandHandler.class);

    @Autowired
    @Qualifier("orderRepository")
    private Repository<Order> orderRepository;

    @Autowired
    @Qualifier("productRepository")
    private Repository<Product> productRepository;

    @CommandHandler
    public void handleNewOrder(NewOrderCommand newOrderCommand){

        LOGGER.info("Start");
    	Product product = productRepository.load(newOrderCommand.getProductId());
    	product.depreciateStock(newOrderCommand.getNumber());
    	Integer id = new Random().nextInt();
    	Order order = new Order(id, newOrderCommand.getPrice(), newOrderCommand.getNumber(), OrderStatusEnum.NEW, product);
    	orderRepository.add(order);
    	LOGGER.debug("New Order Created with id: {}; Price: {}; Numbers: {} of Product ID: {}", new Object[]{id, newOrderCommand.getPrice(), newOrderCommand.getNumber(), product.getId()});
        LOGGER.info("End");
    }

    @CommandHandler
    public void handleUpdateOrder(OrderStatusUpdateCommand orderStatusUpdateCommand){

    	LOGGER.info("Start");
    	Order order = orderRepository.load(orderStatusUpdateCommand.getOrderId());
    	order.updateOrderStatus(orderStatusUpdateCommand.getOrderStatus());
    	LOGGER.debug("Order with id: {} updated with status: {}", new Object[]{orderStatusUpdateCommand.getOrderId(), orderStatusUpdateCommand.getOrderStatus()});
        LOGGER.info("End");
    }

}