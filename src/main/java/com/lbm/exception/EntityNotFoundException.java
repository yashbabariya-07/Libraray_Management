package com.lbm.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String roleName, String id){
        super(roleName + " with ID '" + id + "' not found!");
    }
}

