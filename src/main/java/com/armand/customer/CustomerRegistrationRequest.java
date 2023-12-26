package com.armand.customer;

public record CustomerRegistrationRequest (
        String name,
        String email,
        Integer age
){}
