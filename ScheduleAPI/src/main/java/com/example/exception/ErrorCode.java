package com.example.exception;

import lombok.Getter;

//에러코드 정의

@Getter
public enum ErrorCode {
	
	  	INVALID_INPUT_VALUE(400,"Invalid Input Value"),
	    METHOD_NOT_ALLOWED(405,  "Invalid Input Value"),
	    HANDLE_ACCESS_DENIED(403, "Access is Denied"),	  
	    INTERNAL_SERVER_ERROR(500, "Internal Server error");
	    
		private int statusCode;
	    private final String message;
		

	    ErrorCode(final int statusCode, final String message) {
	        this.statusCode = statusCode;
	        this.message = message;
	    }
	
}//enum end
