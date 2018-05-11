package com.amazon.mShop.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/cucumber" },
        glue = "com.amazon.mShop.steps",
        //glue = "com.amazon.mShop",
        features = "classpath:cucumber/AmazonMShop_VerifyProductSearchResultsVisible.feature"
)
public class RunAmazonMApp_Test {
}
