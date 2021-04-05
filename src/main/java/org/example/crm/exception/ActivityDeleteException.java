package org.example.crm.exception;

/**
 * 市场活动删除异常
 */
public class ActivityDeleteException extends RuntimeException{

    public ActivityDeleteException() {
        super();
    }

    public ActivityDeleteException(String message) {
        super(message);
    }
}
