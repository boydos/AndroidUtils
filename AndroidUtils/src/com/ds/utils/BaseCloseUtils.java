package com.ds.utils;

import java.io.Closeable;
import java.io.IOException;

public class BaseCloseUtils {
	public static void closeResources(Closeable ...clbs) {
		try {
			if(clbs!=null){
				for(Closeable clb:clbs ) {
					if(clb!=null)clb.close();
				}
			};
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
