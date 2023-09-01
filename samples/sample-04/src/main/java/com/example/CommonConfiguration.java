package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangshuai
 * @create 2023-09-01-10:22
 */
@Configuration
public class CommonConfiguration {
	public static final String NAMESPACE_KEY = "apollo.namespace";

	@Value("${apollo.bootstrap.namespaces}")
	private String apolloNamespace;



	// get apolloNamespace value
	public String getApolloNamespace(){
		return apolloNamespace;
	}
}
