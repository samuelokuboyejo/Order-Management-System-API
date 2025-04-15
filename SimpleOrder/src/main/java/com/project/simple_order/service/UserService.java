package com.project.simple_order.service;

import com.project.simple_order.auth.entities.User;
import com.project.simple_order.utils.AppResponse;

public interface UserService {

    User getAuthenticatedUser();
    AppResponse getUserDetailsWithOrderHistory();
}
