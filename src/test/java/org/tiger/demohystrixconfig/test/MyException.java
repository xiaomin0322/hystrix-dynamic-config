package org.tiger.demohystrixconfig.test;

import org.tiger.demohystrixconfig.Configer;

public class MyException extends Exception {
	
	public String config = Configer.val;

}
