package com.pvmsys.brix.graph.beans;

public interface EnumReturnCode 
{
	
	final int I_SUCCESS = 1;
	final int I_FAILURE = 2;
	final int I_ERROR = 3;
	final int I_EXCEPTION = 4;
	final int I_DUPLICATE = 6;
	final int I_INUSED = 7;
	final int I_INVALID = 8;
	final int I_INVALID_UNAUTHORIZED_KEY= 9;
	final int I_REQ_UNAUTHORIZED= 401;
	final int I_REQ_SUCCESS= 200;
	final int I_SESSION_KEY_EXPIRE= 403;
	final int I_NOT_FOUND = 10;
	
}  
